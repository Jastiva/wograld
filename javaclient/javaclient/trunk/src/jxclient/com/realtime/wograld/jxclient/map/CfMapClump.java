/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realtime.wograld.jxclient.map;
import java.util.Deque;

/**
 *
 * @author jastiv
 */
public class CfMapClump {
    
    private Deque<CfMapSquare> CfMapSquaresLocal;
    
    private int max_box_x;
    
    private int min_box_x;
    
    private int max_box_y;
    
    private int min_box_y;
    
    public CfMapClump(CfMapSquare mapsquare6){
        int tile_x = mapsquare6.getX();
        int tile_y = mapsquare6.getY();
        int screen_x = tile_x + tile_y - 10;
        int screen_y = tile_y - tile_x + 8;
        min_box_x = screen_x;
        max_box_x = screen_x + 1;
        min_box_y = screen_y;
        max_box_y = screen_y + 1;
        CfMapSquaresLocal.push(mapsquare6);
    }
    
    public CfMapSquare releaseMapSquare(){
        CfMapSquare tmp = CfMapSquaresLocal.pop();
        return tmp; 
    }
    
    public boolean nonEmptyClump(){
        return !CfMapSquaresLocal.isEmpty();
    }
    
    public boolean isFar(CfMapSquare mapsquare7){
        int tile_x = mapsquare7.getX();
        int tile_y = mapsquare7.getY();
        int screen_x = tile_x + tile_y - 10;
        int screen_y = tile_y - tile_x + 8;
        if((min_box_x - 2 <= screen_x) && (screen_x + 3 <= max_box_x) &&
         (min_box_y - 1 <= screen_y) && (screen_y + 3 <= max_box_y)) {
            return false;
        }
        else {
            return true;
        }
        
    }
    
    public boolean is_inside_inprogress_clump(CfMapSquare mapsquare4){
        int tile_x = mapsquare4.getX();
        int tile_y = mapsquare4.getY();
        int screen_x = tile_x + tile_y - 10;
        int screen_y = tile_y - tile_x + 8;
        if((min_box_x <= screen_x) && (screen_x + 1 <= max_box_x) &&
         (min_box_y <= screen_y) && (screen_y + 1 <= max_box_y)) {
            if(CfMapSquaresLocal.contains(mapsquare4)){
                return true;
            } else {
                return false;  /* the final form of the clump may still contain it */
            }
            
        } else {
            return false; /* the final form of the clump may still contain it */
        }
    }
    
    public void addToClump(CfMapSquare mapsquare5){
        int tile_x = mapsquare5.getX();
        int tile_y = mapsquare5.getY();
        int screen_x = tile_x + tile_y - 10;
        int screen_y = tile_y - tile_x + 8;
        if((screen_x + 1) > max_box_x ){
            max_box_x = screen_x + 1;
        }
        if(screen_x < min_box_x) {
            min_box_x = screen_x;
        }
        if((screen_y + 1) > max_box_y) {
            max_box_y = screen_y + 1;
        }
        if(screen_y < min_box_y){
            min_box_y = screen_y;
        }
        CfMapSquaresLocal.push(mapsquare5);
    }
    
    public void mergeClumps(CfMapClump foobar){
        if(foobar != this){  /* prevent infinite loop */
            while (foobar.nonEmptyClump()) {
                CfMapSquare baz = foobar.releaseMapSquare();
                addToClump(baz);
            }
        }
        /* when done, caller can free foobar */
        /* at the moment does not recalculate foobar box when removing its element */
        /* so do not rely upon bounding box in between removing elements or */
        /* try to reuse between consuming and trying to delete */
    }
    
    
    
   
    
    
}
