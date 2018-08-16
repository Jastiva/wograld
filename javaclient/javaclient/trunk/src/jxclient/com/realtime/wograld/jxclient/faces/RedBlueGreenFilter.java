/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realtime.wograld.jxclient.faces;

import java.awt.image.RGBImageFilter;
 
 
 

/**
 *
 * @author jastiv; thank Sun for http://download.java.net/jdk7/archive/b123/docs/api/java/awt/image/RGBImageFilter.html
 */
public class RedBlueGreenFilter extends RGBImageFilter {
    
    public RedBlueGreenFilter() {
              // The filter's operation does not depend on the
              // pixel's location, so IndexColorModels can be
              // filtered directly.
              canFilterIndexColorModel = true;
          }

          public int filterRGB(int x, int y, int rgb) {
              return ((rgb & 0xff00ff00)
                      | ((rgb & 0xff0000) >> 16)
                      | ((rgb & 0xff) << 16));
          }
    
}
