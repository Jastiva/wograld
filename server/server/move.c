/*
 * static char *rcsid_move_c =
 *    "$Id: move.c,v 1.4 2012/09/28 21:14:01 serpentshard Exp $";
 */

/*
    CrossFire, A Multiplayer game for X-windows

    Copyright (C) 2002 Mark Wedel & Wograld Development Team
    Copyright (C) 1992 Frank Tore Johansen

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

    The author can be reached via e-mail to wograld-devel@real-time.com
*/

#include <global.h>
#ifndef __CEXTRACT__
#include <sproto.h>
#endif

static int roll_ob(object *op, int dir, object *pusher);

/**
 * move_object() tries to move object op in the direction "dir".
 * If it fails (something blocks the passage), it returns 0,
 * otherwise 1.
 * This is an improvement from the previous move_ob(), which
 * removed and inserted objects even if they were unable to move.
 */

int move_object(object *op, int dir) {
    return move_ob(op, dir, op);
}


/**
 * object op is trying to move in direction dir.
 * originator is typically the same as op, but
 * can be different if originator is causing op to
 * move (originator is pushing op)
 * returns 0 if the object is not able to move to the
 * desired space, 1 otherwise (in which case we also 
 * move the object accordingly.  This function is
 * very similiar to move_object.
  */
int move_ob (object *op, int dir, object *originator)
{
    sint16 newx = op->x+freearr_x[dir];
    sint16 newy = op->y+freearr_y[dir];
    object *tmp;
    mapstruct *m;
    int mflags;

	static int oldmusic=0;
	static int mnum=0;
	mapstruct *old_m;
	mapstruct *test_m;
    if(op==NULL) {
	LOG(llevError,"Trying to move NULL.\n");
	return 0;
    }

    m = op->map;
    mflags = get_map_flags(m, &m, newx, newy, &newx, &newy);

    /* If the space the player is trying to is out of the map,
     * bail now - we know it can't work.
     */
    if (mflags & P_OUT_OF_MAP) return 0;


    /* Is this space blocked?  Players with wizpass are immune to
     * this condition.
     */
    if(blocked_link(op, m, newx, newy) &&
       !QUERY_FLAG(op, FLAG_WIZPASS))
	return 0;

    /* 0.94.2 - we need to set the direction for the new animation code.
     * it uses it to figure out face to use - I can't see it
     * breaking anything, but it might.
     */
    if(op->more != NULL && !move_ob(op->more, dir, op->more->head))
	return 0;

    op->direction = dir;

    if(op->will_apply&4)
	check_earthwalls(op,m, newx,newy);
    if(op->will_apply&8)
	check_doors(op,m, newx,newy);

    /* 0.94.1 - I got a stack trace that showed it crash with remove_ob trying
     * to remove a removed object, and this function was the culprit.  A possible
     * guess I have is that check_doors above ran into a trap, killing the
     * monster.
     *
     * Unfortunately, it doesn't appear that the calling functions of move_object
     * deal very well with op being killed, so all this might do is just
     * migrate the problem someplace else.
     */

    if (QUERY_FLAG(op, FLAG_REMOVED)) {
	LOG(llevDebug,"move_object: monster has been removed - will not process further\n");
	/* Was not successful, but don't want to try and move again */
	return 1;
    }

    /* If this is a tail portion, just want to tell caller that move is
     * ok - the caller will deal with actual object removal/insertion
     */
    if(op->head)
	return 1;

	//old_m = op->map;
	//oldmusic=old_m->tracknum;
test_m =  get_map_from_coord(op->map, &newx, &newy);
	if(test_m)
	{
		oldmusic=mnum;
		mnum=test_m->tracknum;

	}
	else
	{
		mnum=0;
	}

    remove_ob(op);

    /* we already have newx, newy, and m, so lets use them.
     * In addition, this fixes potential crashes, because multipart object was
     * on edge of map, +=x, +=y doesn't make correct coordinates.
     */
    for(tmp = op; tmp != NULL; tmp = tmp->more) {
	tmp->x += freearr_x[dir];
	tmp->y += freearr_y[dir];
	tmp->map =  get_map_from_coord(tmp->map, &tmp->x, &tmp->y);
    }

    /* insert_ob_in_map will deal with any tiling issues */
    insert_ob_in_map(op, m, originator,0);

	

    /* Hmmm.  Should be possible for multispace players now */
    if (op->type==PLAYER) {
	esrv_map_scroll(&op->contr->socket, freearr_x[dir],freearr_y[dir]);
	apply_gravity(op);
	op->contr->socket.update_look=1;
	op->contr->socket.look_position=0;
	printf("mnum %i oldmusic %i\n",mnum,oldmusic);
        if((mnum>0)&&(mnum!=oldmusic))
        {
	printf("tilemap music %i\n",mnum);
        send_change_music( op->contr, mnum );
	}

}

    else if (op->type == TRANSPORT) {
	object *pl;

	for (pl=op->inv; pl; pl=pl->below) {
	    if (pl->type == PLAYER) {
		pl->contr->do_los=1;
		pl->map = op->map;
		pl->x = op->x;
		pl->y = op->y;
		esrv_map_scroll(&pl->contr->socket, freearr_x[dir],freearr_y[dir]);
		pl->contr->socket.update_look=1;
		pl->contr->socket.look_position=0;
	    }
	}
    }

	

    return 1;	/* this shouldn't be reached */
}


/**
 * transfer_ob(): Move an object (even linked objects) to another spot
 * on the same map.
 *
 * Does nothing if there is no free spot.
 *
 * randomly: If true, use find_free_spot() to find the destination, otherwise
 * use find_first_free_spot().
 *
 * Return value: 1 if object was destroyed, 0 otherwise.
 */

int transfer_ob (object *op, int x, int y, int randomly, object *originator)
{
    int i;
    object *tmp;

    if (randomly)
	i = find_free_spot (op,op->map,x,y,0,SIZEOFFREE);
    else
	i = find_first_free_spot(op,op->map,x,y);

    if (i==-1)
	return 0;	/* No free spot */

    if(op->head!=NULL)
	op=op->head;
    remove_ob(op);
    for(tmp=op;tmp!=NULL;tmp=tmp->more)
	tmp->x=x+freearr_x[i]+(tmp->arch==NULL?0:tmp->arch->clone.x),
	tmp->y=y+freearr_y[i]+(tmp->arch==NULL?0:tmp->arch->clone.y);

    tmp = insert_ob_in_map(op,op->map,originator,0);
	 if (op && op->type == PLAYER)
        {
         map_newmap_cmd(op->contr);
          int mnum=0;
        if(op->map)
        {
         mnum=op->map->tracknum;
        }
        send_change_music( op->contr, mnum );

        }
    if (tmp) return 0;
    else return 1;
}

/**
 * Return value: 1 if object was destroyed, 0 otherwise.
 * Modified so that instead of passing the 'originator' that had no
 * real use, instead we pass the 'user' of the teleporter.  All the
 * callers know what they wanted to teleporter (move_teleporter or
 * shop map code)
 * tele_type is the type of teleporter we want to match against -
 * currently, this is either set to SHOP_MAT or TELEPORTER.
 * It is basically used so that shop_mats and normal teleporters can
 * be used close to each other and not have the player put to the
 * one of another type.
 */
int teleport (object *teleporter, uint8 tele_type, object *user)
{
    object *altern[120]; /* Better use c/malloc here in the future */
    int i,j,k,nrofalt=0;
    object *other_teleporter, *tmp;
    mapstruct *m;
    sint16  sx, sy;

    if(user==NULL) return 0;
    if(user->head!=NULL)
	user=user->head;

    /* Find all other teleporters within range.  This range
     * should really be setable by some object attribute instead of
     * using hard coded values.
     */
    for(i= -5;i<6;i++)
	for(j= -5;j<6;j++) {
	    if(i==0&&j==0)
		continue;
	    /* Perhaps this should be extended to support tiled maps */
	    if(OUT_OF_REAL_MAP(teleporter->map,teleporter->x+i,teleporter->y+j))
		continue;
	    other_teleporter=get_map_ob(teleporter->map,
                                  teleporter->x+i,teleporter->y+j);

	    while (other_teleporter) {
		if (other_teleporter->type == tele_type) break;
		other_teleporter = other_teleporter->above;
	    }
	    if (other_teleporter)
		altern[nrofalt++]=other_teleporter;
	}

    if(!nrofalt) {
	LOG(llevError,"No alternative teleporters around!\n");
	return 0;
    }

    other_teleporter=altern[RANDOM()%nrofalt];
    k=find_free_spot(user,other_teleporter->map,
                        other_teleporter->x,other_teleporter->y,1,9);

    /* if k==-1, unable to find a free spot.  If this is shop
     * mat that the player is using, find someplace to move
     * the player - otherwise, player can get trapped in the shops
     * that appear in random dungeons.  We basically just make
     * sure the space isn't no pass (eg wall), and don't care
     * about is alive.
     */
    if (k==-1) {
	if (tele_type == SHOP_MAT && user->type == PLAYER) {
	    for (k=1; k<9; k++) {
		if (get_map_flags(other_teleporter->map, &m, 
			other_teleporter->x + freearr_x[k],
			other_teleporter->y + freearr_y[k], &sx,&sy) &
			P_OUT_OF_MAP) continue;

		if (!OB_TYPE_MOVE_BLOCK(user, GET_MAP_MOVE_BLOCK(m, sx, sy))) break;

	    }
	    if (k==9) {
		LOG(llevError,"Shop mat %s (%d, %d) is in solid rock?\n",
		    other_teleporter->name, other_teleporter->x, other_teleporter->y);
		return 0;
	    }
	}
	else return 0;
    }

    remove_ob(user);

    /* Update location for the object */
    for(tmp=user;tmp!=NULL;tmp=tmp->more) {
	tmp->x=other_teleporter->x+freearr_x[k]+
           (tmp->arch==NULL?0:tmp->arch->clone.x);
	tmp->y=other_teleporter->y+freearr_y[k]+
           (tmp->arch==NULL?0:tmp->arch->clone.y);
    }
    tmp = insert_ob_in_map(user,other_teleporter->map,NULL,0);
	  if (tmp && tmp->type == PLAYER)
        {
         map_newmap_cmd(tmp->contr);
          int mnum=0;
        if(tmp->map)
        {
         mnum=tmp->map->tracknum;
        }
        send_change_music( tmp->contr, mnum );

        }
    return (tmp == NULL);
}

void recursive_roll(object *op,int dir,object *pusher) {
  if(!roll_ob(op,dir,pusher)) {
    new_draw_info_format(NDI_UNIQUE, 0, pusher,
	"You fail to push the %s.",query_name(op));
    return;
  }
  (void) move_ob(pusher,dir,pusher);
  new_draw_info_format(NDI_BLACK, 0, pusher,
	"You move the %s.",query_name(op));
  return;
}

/**
 * This is a new version of blocked, this one handles objects
 * that can be passed through by monsters with the CAN_PASS_THRU defined.
 *
 * very new version handles also multipart objects
 * This is currently only used for the boulder roll code.
 * Returns 1 if object does not fit, 0 if it does.
 */

static int try_fit (object *op, mapstruct *m, int x, int y) 
{
    object *tmp, *more;
    sint16 tx, ty;
    int mflags;
    mapstruct *m2;

    if (op->head) 
	op = op->head;

    for (more = op; more ; more = more->more) {
	tx = x + more->x - op->x;
	ty = y + more->y - op->y;

	mflags = get_map_flags(m, &m2, tx, ty, &tx, &ty);

	if (mflags & P_OUT_OF_MAP)
	    return 1;

	for (tmp = get_map_ob (m2, tx, ty); tmp; tmp=tmp->above) {
	    if (tmp->head == op || tmp == op)
		continue;

	    if ((QUERY_FLAG(tmp,FLAG_ALIVE) && tmp->type!=DOOR))
		return 1;

	    if (OB_MOVE_BLOCK(op, tmp)) return 1;

	}
    }
    return 0;
}

/**
 * this is not perfect yet. 
 * it does not roll objects behind multipart objects properly.
 * Support for rolling multipart objects is questionable.
 */

static int roll_ob(object *op,int dir, object *pusher) {
    object *tmp;
    sint16 x, y;
    int flags;
    mapstruct *m;
    MoveType	move_block;

    if (op->head) 
	op = op->head;

    x=op->x+freearr_x[dir];
    y=op->y+freearr_y[dir];

    if(!QUERY_FLAG(op,FLAG_CAN_ROLL) || 
       (op->weight &&
       random_roll(0, op->weight/50000-1, pusher, PREFER_LOW) > pusher->stats.Str))
	return 0;

    m = op->map;
    flags = get_map_flags(m, &m, x, y, &x, &y);

    if (flags & (P_OUT_OF_MAP | P_IS_ALIVE))
	return 0;

    move_block = GET_MAP_MOVE_BLOCK(m, x, y);

    /* If the target space is not blocked, no need to look at the objects on it */
    if ((op->move_type & move_block) == op->move_type) {
	for (tmp=get_map_ob(m, x, y); tmp!=NULL; tmp=tmp->above) {
	if (tmp->head == op)
	    continue;
	if (OB_MOVE_BLOCK(op, tmp) && !roll_ob(tmp,dir,pusher))
	    return 0;
	}
    }
    if (try_fit (op, m, x, y))
	return 0;

    remove_ob(op);
    for(tmp=op; tmp!=NULL; tmp=tmp->more)
	tmp->x+=freearr_x[dir],tmp->y+=freearr_y[dir];
    insert_ob_in_map(op,op->map,pusher,0);
    apply_gravity(op);
    return 1;
}

/** returns 1 if pushing invokes a attack, 0 when not */
int push_ob(object *who, int dir, object *pusher) {
    int str1, str2;
    object *owner;

    if (who->head != NULL)
	who = who->head;
    owner = get_owner(who);

    /* Wake up sleeping monsters that may be pushed */
    CLEAR_FLAG(who,FLAG_SLEEP);
  
    /* player change place with his pets or summoned creature */
    /* TODO: allow multi arch pushing. Can't be very difficult */
    if (who->more == NULL && owner == pusher) {
	int temp;
	mapstruct *m;

	remove_ob(who);
	remove_ob(pusher);
	temp = pusher->x;
	pusher->x = who->x;
	who->x = temp;

	temp = pusher->y;
	pusher->y = who->y;
	who->y = temp;

	m = pusher->map;
	pusher->map = who->map;
	who->map = m;

	insert_ob_in_map (who,who->map,pusher,0);
	insert_ob_in_map (pusher,pusher->map,pusher,0);

	/* we presume that if the player is pushing his put, he moved in
	 * direction 'dir'.  I can' think of any case where this would not be
	 * the case.  Putting the map_scroll should also improve performance some.
	 */
	if (pusher->type == PLAYER ) {
	    esrv_map_scroll(&pusher->contr->socket, freearr_x[dir],freearr_y[dir]);
	    pusher->contr->socket.update_look=1;
	    pusher->contr->socket.look_position=0;
	}
	return 0;
    }


    /* We want ONLY become enemy of evil, unaggressive monster. We must RUN in them */
    /* In original we have here a unaggressive check only - that was the reason why */
    /* we so often become an enemy of friendly monsters... */
    /* funny: was they set to unaggressive 0 (= not so nice) they don't attack */

    if(owner != pusher &&  pusher->type == PLAYER && who->type != PLAYER &&
      !QUERY_FLAG(who,FLAG_FRIENDLY)&& !QUERY_FLAG(who,FLAG_NEUTRAL)) {
	if(pusher->contr->run_on) /* only when we run */ {
	    new_draw_info_format(NDI_UNIQUE, 0, pusher,
              "You start to attack %s !!",who->name);
	    CLEAR_FLAG(who,FLAG_UNAGGRESSIVE); /* the sucker don't like you anymore */
	    who->enemy = pusher;
	    return 1;
	}
	else 
	{
	    new_draw_info_format(NDI_UNIQUE, 0, pusher,
				 "You avoid attacking %s .",who->name);
	}
    }

    /* now, lets test stand still. we NEVER can push stand_still monsters. */
    if(QUERY_FLAG(who,FLAG_STAND_STILL))
    {
	new_draw_info_format(NDI_UNIQUE, 0, pusher,
          "You can't push %s.",who->name);
	return 0;
    }
  
    /* This block is basically if you are pushing friendly but
     * non pet creaturs.
     * It basically does a random strength comparision to
     * determine if you can push someone around.  Note that
     * this pushes the other person away - its not a swap.
     */

    str1 = (who->stats.Str>0?who->stats.Str:who->level);
    str2 = (pusher->stats.Str>0?pusher->stats.Str:pusher->level);
    if(QUERY_FLAG(who,FLAG_WIZ) ||
       random_roll(str1, str1/2+str1*2, who, PREFER_HIGH) >= 
       random_roll(str2, str2/2+str2*2, pusher, PREFER_HIGH) ||
       !move_object(who,dir))
    {
	if (who ->type == PLAYER) {
	    new_draw_info_format(NDI_UNIQUE, 0, who,
		 "%s tried to push you.",pusher->name);
	}
	return 0;
    }

    /* If we get here, the push succeeded.
     * Let everyone know the status.
     */
    if (who->type == PLAYER) {
	new_draw_info_format(NDI_UNIQUE, 0, who,
			     "%s pushed you.",pusher->name);
    }
    if (pusher->type == PLAYER) {
	new_draw_info_format(NDI_UNIQUE, 0, pusher,
		"You pushed %s back.", who->name);
    }
  
    return 1;
}


void apply_gravity(object *faller)
{
	int canfall;
	int mlayer;
	object *temp_ob;
	mapstruct *ml1, *oldmap;
	int oldx, oldy;

	oldx = faller->x;
	oldy = faller->y;
	oldmap = faller->map;

//	if(((faller->type == PLAYER) || (faller->flags && FLAG_CAN_ROLL)) && (faller->move_type != MOVE_FLYING))
        if(((faller->type == PLAYER) || (faller->type == TRANSPORT) || (faller->flags && FLAG_ALIVE) || (faller->flags && FLAG_CAN_ROLL)) && (faller->move_type != MOVE_FLYING))
	{
		canfall = 0;
		
		for(mlayer = 0; mlayer < MAP_LAYERS; mlayer++)
		{
              // this is a bug, are you sure you should not use lists per tile of objects not from viewport?
			temp_ob = (GET_MAP_FACE_OBJ(faller->map, faller->x, faller->y, mlayer));
			if(temp_ob != 0)
			{
			/* if((temp_ob -> type) && FLOOR){ */
                	  
			if (temp_ob->face->number !=  find_face("empty.111",0))
			{
				canfall += 1;
			}
			else
			{
  				printf("found empty\n");
			} 
				/* printf("%i\n",555555); */
			/* } */
			}
	/* 	printf("%i\n",canfall); */
		}
		if(canfall < 2)
		{
		 /* printf("%i\n",666666); */ 
			if(faller->map->lower)
			{
				ml1 = load_and_link_lower_map(faller->map);
				printf("%d\n",ml1);
				/* if(GET_MAP_MOVE_BLOCK(ml1, faller->x, faller->y) == MOVE_ALL) */
				if(!blocked_link(faller, ml1, faller->x, faller->y))
				{
					/* printf("%i\n",777777); */
					if(faller->type == PLAYER)
					{
						fall_enter(faller, ml1);
						if((faller->x != oldx) || (faller->y !=oldy))
						{
							remove_ob(faller);
							insert_ob_in_map(faller,oldmap, oldx, oldy);
					 
						}
					}
					else
					{
						if(faller->flags && FLAG_CAN_ROLL)
						{
					/*		printf("%i\n",888888); */
						 	remove_ob(faller);
  							 insert_ob_in_map(faller, ml1, faller,0);
							if((faller->x != oldx) || (faller->y !=oldy))
							{
								remove_ob(faller);
								insert_ob_in_map(faller,oldmap, oldx, oldy);
					 
							}
							else
							{
								apply_gravity(faller);
							}
						}

					}
				}
			}
		}
		
	}
}


int try_elevate_enter(object *pushed)
{

int canraise;
        int mlayer;
        object *temp_ob;
        mapstruct *mu1, *oldmap;
        int oldx, oldy;

        oldx = pushed->x;
        oldy = pushed->y;
        oldmap = pushed->map;


  if((pushed->type == PLAYER) || (pushed->flags && FLAG_ALIVE) || (pushed->flags && FLAG_CAN_ROLL) || (pushed->type == TRANSPORT))
    {
   canraise=0;

    if(pushed->map->upper)
      {

        mu1 = load_and_link_upper_map(pushed->map);
        for(mlayer = 0; mlayer < MAP_LAYERS; mlayer++)
        {
           temp_ob=(GET_MAP_FACE_OBJ(mu1, pushed->x, pushed->y, mlayer));
           if(temp_ob != 0)
           { 
                         // at the moment do not push monsters and items up into player
                 if(temp_ob->face->number != find_face("empty.111",0))
                 {
                     canraise +=1;
                 }
           }
        }
      }
      else
      {
	printf("no upper floor for elevator\n");
        return 0;
      }

      if(canraise < 1)
      {
            mu1=load_and_link_upper_map(pushed->map);

		if(pushed->type == PLAYER)
                                        {
                                       printf("moved player to upper map\n");
                                               fall_enter(pushed, mu1);
                                             // enter_map(pushed, mu1, pushed->x, (pushed->y)-1);
                                            /*    if((pushed->x != oldx) || (pushed->y !=oldy))
                                                {
                                                        remove_ob(pushed);
                                                        insert_ob_in_map(pushed,oldmap, oldx, oldy);

                                                }
                                          */
                                                printf("tried to add player char to above map\n");
                                        }
                                        else
                                        {
                                             //   if(faller->flags && FLAG_CAN_ROLL)
                                              //  {
                                        /*              printf("%i\n",888888); */
                                                        remove_ob(pushed);
                                                     //    insert_ob_in_map(pushed, mu1, pushed,0); WHAT?
                                               //       insert_ob_in_map(pushed,mu1,pushed->x, pushed->y);
                                           /*             if((pushed->x != oldx) || (pushed->y !=oldy))
                                                        {
                                                                remove_ob(pushed);
                                                                insert_ob_in_map(pushed,oldmap, oldx, oldy);

                                                        }
                                            */
                                              insert_ob_in_map_at(pushed,mu1,NULL,0,pushed->x,pushed->y);
                                                     printf("moved rollable to above map\n"); 
                                              //  }

                                        }
                                    return 1;


       }
       else
       {
          printf("floor or item blocks movement upward\n");
          return 0;
       }
    }
    else
    {
          printf("not a type that can be pushed upward yet, items will be examined soon\n");
         return 0;
    }
}


void check_above_for_gravity( mapstruct *drop, int x, int y)
{
mapstruct *mu1;
int mlayer,nlayer;
object *tmp_ob, *first_ob, *floor_ob;
     if(drop->upper)
      {
         mu1 = load_and_link_upper_map(drop);
         mlayer=0;
//         first_ob=(GET_MAP_FACE_OBJ(mu1, x, y, mlayer));
 //        if(first_ob != NULL)
  //       {   
	  for(mlayer = 0; mlayer < MAP_LAYERS; mlayer++)
                {
              // this is a bug, are you sure you should not use lists per tile of objects not from viewport?
                        tmp_ob = (GET_MAP_FACE_OBJ(mu1, x, y, mlayer));
                        if(tmp_ob != 0)
	                       {
        			printf("found something to try drop\n");      
             // for(tmp_ob=first_ob; tmp_ob!=NULL; tmp_ob=tmp_ob->above)
             // {
                   if(((tmp_ob->type == PLAYER) || (tmp_ob->type == TRANSPORT) || (tmp_ob->flags && FLAG_ALIVE) || (tmp_ob->flags && FLAG_CAN_ROLL)) && (tmp_ob->move_type != MOVE_FLYING))
                   {
                        // for(floor_ob=first_ob; floor_ob!=NULL; floor_ob=floor_ob->above)
                       //  {
                        for(nlayer=0; nlayer < MAP_LAYERS; nlayer++)
                        {
                            floor_ob=GET_MAP_FACE_OBJ(mu1,x,y,nlayer);
                            if(floor_ob !=0)
                             {
                      		if(nlayer == mlayer)
                                { 
                                 printf("no floors found under current object\n");
                               //good, try to drop it
                              			 
                                    if(tmp_ob->type == PLAYER)
                                        {
                                                fall_enter(tmp_ob, drop);
                                         }
                                         else
                                        {
                                       		if((tmp_ob->flags && FLAG_ALIVE) || (tmp_ob->flags && FLAG_CAN_ROLL))
                                                 {
						   remove_ob(tmp_ob);
                                                 
                                              insert_ob_in_map_at(tmp_ob,drop,NULL,0,x,y);
                                                 }
                                         }
                                         printf("dropped item, refresh list\n");
                                         break;
                                         //first_ob=GET_MAP_FACE_OBJ(mu1,x,y,mlayer);
                                         //tmp_ob=first_ob;
                                         //break;
                          		// elevator below already cleared blocked flag 



                             }
                             else
                             {
                                 if(floor_ob->face->number != find_face("empty.111",0))
                                 {
                                  // check again the floor tiles
                                     continue;
                                 }
                                 else
                                 {
                                       // fail
                                       printf("floor found under player or rollable to drop\n");
                                       break;
                                       // try for next item to drop, but this should fail also
                                     
                                 }


                             }
                            }
                         }               
                   }

              }  // tmp_ob
             
             
          }
      }
// for(tmp=op->above;tmp!=NULL && tmp->above!=NULL;tmp=tmp->above);

}



