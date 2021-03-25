native class array<T> 
{
  
  native array();         
  native array(int size); 
  
  native void add(T element);
  native T get(int index);
  native T set(int index, T element);
  
  native int size();
  native boolean is_empty();
  
}
