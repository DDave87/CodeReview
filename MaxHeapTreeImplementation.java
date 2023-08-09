# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
PARENT: 6 LEFT CHILD: 4 RIGHT CHILD: 5
PARENT: 4 LEFT CHILD: 2 RIGHT CHILD: 3
PARENT: 5 LEFT CHILD: 1 RIGHT CHILD: 5
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

public class MaxHeapTreeImplementation {

  int size;
  int maxSize;
  int[] maxHeap;

  MaxHeapTreeImplementation(int maxSize) {
    this.size = 0;
    this.maxSize = maxSize;
    this.maxHeap = new int[this.maxSize + 1];
    maxHeap[0] = Integer.MAX_VALUE;

  }

  public static void main(String[] args) {

    MaxHeapTreeImplementation obj = new MaxHeapTreeImplementation(10);

    obj.insert(4);
    obj.insert(3);
    obj.insert(1);
    obj.insert(2);
    obj.insert(7);
    obj.insert(5);
    obj.insert(6);
    obj.display();

    //delete the root node
    System.out.println(obj.delete(1));
    System.out.println();
    obj.display();

  }

  private void display() {
    for (int i = 1; i <= size / 2; i++) {
      System.out.print(
        "PARENT: " + maxHeap[i] +
        " LEFT CHILD: " + maxHeap[2 * i] +
        " RIGHT CHILD: " + maxHeap[(2 * i) + 1]
      );
      System.out.println();
    }
  }

  private void insert(int element) {
    maxHeap[++size] = element;
    int index = size;
    heapifyUP(index);
  }

  private void heapifyUP(int i) {
    while (maxHeap[i] > maxHeap[parent(i)]) {
      swap(i, parent(i));
      i = parent(i);
    }
  }

  private void swap(int fpos, int spos) {
    int temp;
    temp = maxHeap[fpos];
    maxHeap[fpos] = maxHeap[spos];
    maxHeap[spos] = temp;
  }

  private int parent(int i) {
    return i / 2;
  }

  private int leftChild(int i) {
    return 2 * i;
  }

  private int rightChild(int i) {
    return (2 * i + 1);
  }

  private int delete(int pos) {
    int pop = maxHeap[pos];
    maxHeap[pos] = maxHeap[size];
    size--;

    if (pos == 1 || maxHeap[parent(pos)] < maxHeap[pos]) {
      heapifyDown(pos);
    } else {
      heapifyUP(pos);
    }

    return pop;
  }

  private void heapifyDown(int pos) {

    if (isLeaf(pos)) {
      return;
    }

    if (maxHeap[pos] < maxHeap[leftChild(pos)]) {
      swap(pos, leftChild(pos));
      heapifyDown(pos);
    }
    if (maxHeap[pos] < maxHeap[rightChild(pos)]) {
      swap(pos, rightChild(pos));
      heapifyDown(pos);
    }

  }

  private boolean isLeaf(int pos) {

    if (pos > (size / 2))
      return true;
    return false;
  }

}
