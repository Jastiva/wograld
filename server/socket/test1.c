static int do_gravity(object *op);

static int do_gravity(object *op)
{
	object *dummy;
	object *pl;

	mapstruct *m;


	if ( QUERY_FLAG(op, FLAG_WIZ) ) return 0;
	if ( QUERY_FLAG(op,FLAG_FLYING) ) return 0;

	if (op->layer > 5 )   /* so we dont do torches... */
	if (!blocks_verticle_view(op->map,op->x,op->y) )
	{
		/*
		LOG(llevDebug,"do_gravity: %s should fall \n",query_name(op));
		LOG(llevDebug," %s is on layer %d \n",query_name(op),op->layer);
		*/

 		pl = op;
	
		m = op->map->tile_map[8];   /* 8 = down */

		if (m==NULL) return 0;

		dummy = get_object();
		FREE_AND_ADD_REF_HASH(EXIT_PATH(dummy), m->path);
		EXIT_X(dummy) = op->x;
		EXIT_Y(dummy) = op->y;
		enter_exit(op, dummy);
		free_object(dummy);
	}
	return 1;
}

void process_events (mapstruct *map)
{
......
  if (map != NULL && op->map != map)
      continue;
 do_gravity(op);


	/* as we can see here, the swing speed not effected by
	 * object speed BUT the swing hit itself!
	 * This will invoke a kind of delay of the ready swing
	 * until the monster can move again. Note, that a higher
	 * move speed as swing speed will not invoke a faster swing
	 * speed!
	 */
.....
}

mapstruct * get_verticle_map(int level, object *pl)
{
   int cur_level = 0;
   mapstruct *tmp_map = NULL;

   if (pl->map == NULL) return NULL;
   if (level == 0) return pl->map;

   if (level < 0)
    {
      tmp_map = pl->map;
      while (cur_level != level)
        {
          if (!tmp_map->tile_map[8] || tmp_map->tile_map[8]->in_memory != MAP_IN_MEMORY)
                ready_map_name(tmp_map->tile_path[8], MAP_NAME_SHARED);

          tmp_map = tmp_map->tile_map[8]; /* up */
          if (tmp_map==NULL) return NULL;
          cur_level--;
        }
      return tmp_map;
    }
   else
     {
      tmp_map = pl->map;
      while (cur_level != level)
        {
        if (!tmp_map->tile_map[9] || tmp_map->tile_map[9]->in_memory != MAP_IN_MEMORY)
                ready_map_name(tmp_map->tile_path[9], MAP_NAME_SHARED);

          tmp_map = tmp_map->tile_map[9]; /* down */
         if (tmp_map==NULL) return NULL;
          cur_level++;
        }
      return tmp_map;
    }
  return NULL; /* shouldn;t get here... */
}

int blocks_verticle_view(mapstruct *m, int x, int y) {
    mapstruct *nm;

    if(!(nm = out_of_map(m, &x, &y)))
                return (P_BLOCKSVERTVIEW|P_NO_PASS|P_OUT_OF_MAP);

    return (GET_MAP_FLAGS(nm,x,y) & P_BLOCKSVERTVIEW);
}

int command_mapstacklimit(object *op, char *params) 
{
  int temp;
  char buf[256];

  if (params!=NULL)
	{
           /*LOG(llevDebug, "R_SAVEMAP: Paramater (%s)\n", params ); */
                                
	   if (strcmp(params,"clear")==0)
            {
           
             op->contr->show_above = 6;
	     op->contr->show_below = 6;             
             new_draw_info(NDI_UNIQUE, 0, op,"Mapstack limit cleared");

	     return 1; 
             }
           else
             {
	         temp = atoi(params);

		 if (temp < 0)  
                   {
                     if (temp >= -10) 
                       {
                          op->contr->show_below = temp;
                          op->contr->update_los = 1;
			}
                   }
                 else if (temp > 0)
                   {
			if (temp <=10) 
                            {
                              op->contr->show_above = temp;
                              op->contr->update_los = 1;
                            }
                   }
	         else  /* temp must be 0 */
                   {
		      op->contr->show_above=0;
		      op->contr->show_below=0;
		      op->contr->update_los = 1;
                   }
             }
       }
  else
       {
         new_draw_info(NDI_UNIQUE, 0, op,"You must give a upper limit, lower limit, or 'clear'");
	 sprintf(buf,"Current: Upper Limit:%d   Lower Litmit:%d",op->contr->show_below,op->contr->show_above);
         new_draw_info(NDI_UNIQUE, 0, op,buf);

	}

  return 1;
}

