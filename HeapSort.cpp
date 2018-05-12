//
// Created by Pagezi on 2018/5/5.
//
#include <iostream>
#include <vector>
using namespace std;
void Max_Heapify(vector<int>& A,int i);
void Build_Max_Heap(vector<int>& A);

//堆排序，每次都把堆顶最大的和最后一个叶子节点交换，然后再调整叶子节点，O(nlgn)
void HeapSort(vector<int>& A)
{
    Build_Max_Heap(A);
    for(int i=A.size()-1;i>=2;i--)
    {
        int temp=A[1];
        cout<<A[1]<<endl;
        A[1]=A[i];
        A[i]=temp;
        A.pop_back();
        Max_Heapify(A,1);
    }
}

//建堆，把每个非叶子节点都按最大堆的性质调整，O(n)
void Build_Max_Heap(vector<int>& A)
{
    int heapSize=A.size()-1;
    for(int i=heapSize/2;i>=1;i--)
        Max_Heapify(A,i);
}

//把位置为i的元素，根据最大堆的性质调整大合理的位置，O(lgn)
void Max_Heapify(vector<int>& A,int i)
{
    int left=2*i;
    int right=2*i+1;
    int largest;
    if(left<A.size()&&A[left]>A[i])
        largest=left;
    else
        largest=i;
    if(right<A.size()&&A[right]>A[largest])
        largest=right;
    if(largest!=i)
    {
        int temp=A[largest];
        A[largest]=A[i];
        A[i]=temp;
        Max_Heapify(A,largest);
    }
}
