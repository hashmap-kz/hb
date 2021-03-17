class array<T> 
{
  ptr<T> data;
  int size;
  int alloc;
  
  array() {
    this.size = 0;
    this.alloc = 2;
    this.data = new ptr<T>(sizeof(T) * this.alloc);
  }
  
  void add(T e) {
    if(size >= alloc) {
      alloc *= 2;
      
      ptr<T> ndata = new ptr<T>(sizeof(T) * this.alloc);
      for(int i = 0; i < size; i += 1) {
        ndata.set(i, data.get(i));
      }
      data.destroy();
      data = ndata;
    }
    data.set(size, e);
    size += 1;
  }
  
  int size() {
    return size;
  }
  
  T get(int index) {
    std.assert_true(index >= 0);
    std.assert_true(index < size);
    return data.get(index);
  }
  
  T set(int index, T e) {
    std.assert_true(index >= 0);
    std.assert_true(index < size);
    return data.set(index, e);
  }
}
