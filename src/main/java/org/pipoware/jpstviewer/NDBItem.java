package org.pipoware.jpstviewer;

/**
 *
 * @author Franck Arnulfo
 */
public abstract class NDBItem  {
  
  public abstract boolean isLeaf();

  public abstract Iterable<NDBItem> getNDBItems();
  
  public void display() {
    
  }
}
