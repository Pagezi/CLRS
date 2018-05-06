# 《算法导论》
## 第四章 最大子数组问题
#### 问题描述
>  给定一个整数数组 nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。  
> 输入: [-2,1,-3,4,-1,2,1,-5,4],  
> 输出: 6  
> 解释: 连续子数组 [4,-1,2,1] 的和最大为 6。

#### 分治法Θ(nlgn)
分治的思想是，把数组A[low..high]划分为规模尽可能相等的两个子数组(比如中点划分),A[low..mid],A[mid+1..high],然后递归求解这两个子数组。有三种可能的情况：
1. 最大子数组在A[low..mid]中

```math
{\mathop{\rm max}\nolimits} Left = \sum\limits_{t = low}^{mid} {{A_t}} 
```
2. 最大子数组在A[mid+1..high]中 

```math
\max Right = \sum\limits_{t = mid + 1}^{high} {{A_t}} 
```

3. 最大子数组跨越了中点mid

```math
\max Cross = \sum\limits_{t = start}^{mid - 1} {{A_t}}  + {A_{mid}} + \sum\limits_{t = mid + 1}^{end} {{A_t}} 
```
第三种情况求解办法就是从mid点出发，分别向左向右去遍历最大的子数组。  
这个分治算法的递归式，其中Θ(n)就是计算跨越中点的最大子数组的时间复杂度：

```math
T\left( n \right) = \left\{ \begin{array}{l}
\Theta (1)\\
2T\left( {n/2} \right) + \Theta (n)
\end{array} \right.
```


```
#include <iostream>
#include <vector>
using namespace std;
int maxCrossMid(vector<int>& nums,int begin,int mid ,int end);
int maxSubArray_help(vector<int>& nums,int begin,int end);

//最大子数组问题入口
int maxSubArray(vector<int>& nums) {
    int n=nums.size();
    if(n==0)
        return 0;
    if(n==1)
        return nums[0];
    return maxSubArray_help(nums,0,n-1);
}
//递归，三种情形
int maxSubArray_help(vector<int>& nums,int begin,int end) {
    if(begin==end)
        return nums[end];
    int mid=(begin+end)/2;
    int max_left=maxSubArray_help(nums,begin,mid);
    int max_right=maxSubArray_help(nums,mid+1,end);
    int max_cross=maxCrossMid(nums,begin,mid,end);
    return max(max(max_left,max_right),max_cross);
}
//跨越中间特殊处理
int maxCrossMid(vector<int>& nums,int begin,int mid ,int end){
    int left_max=INT32_MIN;
    int right_max=INT32_MIN;
    int sum=0;
    for(int i=mid;i>=begin;i--)
    {
        sum+=nums[i];
        if(sum>left_max)
            left_max=sum;
    }
    sum=0;
    for(int i=mid+1;i<=end;i++)
    {
        sum+=nums[i];
        if(sum>right_max)
            right_max=sum;
    }
    return left_max+right_max;
}
```
#### 基于动态规划思想的方法O(n)
对于数组从左到右处理，记录到目前为止，你遍历的某个元素为止的已经处理过的最大子数组的，基于下面的观察，如果已知A[i…j]的最大子数组，那么可以根据如下的性质将解扩展到为A[i…j+1]
的最大子数组
> A[i…j+1]的最大子数组:  
 1.是A[i…j]
的最大子数组  
2.是某个子数组A[m…j+1]，i≤m≤j+1
。

其实我们忘了一个假设，那就是当你处理到Aj+1
的时候，我们已经知道以Aj
结尾的最大子数组。那么再加上一个Aj+1的时候对比大小就可以求出以Aj+1结尾的最大子数组。

```math
su{m_{j + 1}} = \left\{ \begin{array}{l}
\sum\limits_{t = m}^j {{A_t}}  + {A_{j + 1}} & if{\rm{  }}\sum\limits_{t = m}^j {{A_t}}  + {A_{j + 1}} > {A_{j + 1}}\\
{A_{j + 1}} & {\rm{ }}if{\rm{ }}\sum\limits_{t = m}^j {{A_t}}  + {A_{j + 1}} < {A_{j + 1}}
\end{array} \right.
```
每次循环找到以当前下标结尾的最大子数组。但注意！当前下标结尾的最大子数组不一定是全局最大的子数组啊，每次还要经过和全局最大的子数组进行比较，记录下最大的。

```
#include <iostream>
#include <vector>
using namespace std;
int maxSubArray(vector<int>& nums) {
    int n = nums.size();
    if(n == 0)
        return 0;
    if(n == 1)
        return nums[0];
    vector<int> sum(n,0);
    sum[0] = nums[0];
    int max_sum = nums[0];
    for(int i=1;i<n;++i)
    {
        sum[i]=max(sum[i-1]+nums[i],nums[i]);
        max_sum=max(sum[i],max_sum);
    }
    return max_sum;
}
```
## 第六章 堆排序
- 二叉堆是一个数组，可以看成近似是一个完全二叉树。  
- 堆排序是一种O(nlgn)的原址排序（不需要额外的空间）。  
- 在一个数组下标为i的位置，2*i就是左孩子，2*i-1就是右孩子。向下取整i/2就是i的父亲节点  
- 最大堆的意思就是父亲节点大于两个孩子节点，最小堆则相反

![image](https://wx1.sinaimg.cn/mw1024/ada24d1cly1fr1ywr8qx7j218q0lgwpw.jpg)
#### MAX-HEAPIFY维护堆的性质
MAX-HEAPIFY的过程是维护堆的性质，是之后所有操作的关键。i位置上的元素先和自己的左孩子比较看情况记录下相对比较大的元素的下标，然后再用这个下标的元素和右孩子比较。最后根据这个最大的下标是不是自己本身来决定是否交换元素和是否递归。时间复杂度O(lgn)
![image](https://wx2.sinaimg.cn/mw1024/ada24d1cly1fr1z7a1aifj21iy11masx.jpg)

```
//把位置为i的元素，根据最大堆的性质调整大合理的位置，O(lgn)
void Max_Heapify(vector<int>& A,int i)
{
    int left=2*i;
    int right=2*i+1;
    int largest;
    if(left<A.size()-1&&A[left]>A[i])
        largest=left;
    else
        largest=i;
    if(left<A.size()-1&&A[right]>A[largest])
        largest=right;
    if(largest!=i)
    {
        int temp=A[largest];
        A[largest]=A[i];
        A[i]=temp;
        Max_Heapify(A,largest);
    }
}
```
#### BUILD-MAX-HEAP建堆
建堆其实就是从非叶子节点开始调整，不断调用MAX-HEAPIFY维护最大堆的过程  
- 含n个元素的堆的高度(练习6.1.2)：

```math
\left\lfloor {\lg n} \right\rfloor 
```
- 高度为h的堆最多包含的节点数(练习6.3.3)：

```math
\left\lceil {\frac{2}{{{2^{h + 1}}}}} \right\rceil 
```
高度为h的节点上运行MAX-HEAPIFY的代价：

```math
{\rm O}\left( h \right)
```
那么综上所述BUILD-MAX-HEAP的总代价：

```math
\begin{array}{l}
\sum\limits_{h = 0}^{\left\lfloor {\lg n} \right\rfloor } {\left\lceil {\frac{2}{{{2^{h + 1}}}}} \right\rceil } *{\rm O}\left( h \right) = {\rm O}\left( {n*\sum\limits_{h = 0}^{\left\lfloor {\lg n} \right\rfloor } {\frac{h}{{{2^h}}}} } \right) = {\rm O}(n*\sum\limits_{k = 0}^\infty  {h{{(\frac{1}{2})}^h}} ) = O(n)\\
accordance\\
\sum\limits_{k = 0}^\infty  {k{x^k} = \frac{x}{{{{\left( {1 - x} \right)}^2}}}} {\rm{    }\rm{    }\rm{    }}\sum\limits_{k = 0}^\infty  {h{{(\frac{1}{2})}^h} = \frac{{\frac{1}{2}}}{{{{\left( {1 - \frac{1}{2}} \right)}^2}}} = 2} 
\end{array}
```
举了例子：  
![image](http://wx2.sinaimg.cn/mw690/ada24d1cgy1fr20rlhly2j20y81087j7.jpg)
贴个代码：

```
//建堆，把每个非叶子节点都按最大堆的性质调整，O(n)
void Build_Max_Heap(vector<int>& A)
{
    int heapSize=A.size()-1;
    for(int i=heapSize/2;i>=1;i--)
        Max_Heapify(A,i);
}
```
#### HEAPSORT堆排序算法
堆这个完全二叉树的根节点就是这个堆最大or最小的节点，这时候我们输入A[1]，然后我们让A[1]和A[n]进行互相交换，然后A的数组容量-1。这个时候A[n]这个数值就在根节点的位置，然后调用MAX-HEAPIFY把他调整到最佳位置。最大堆按照这样输出的都是倒序。
举个例子：
![image](http://wx4.sinaimg.cn/mw690/ada24d1cgy1fr20xv9p8dj216811gah4.jpg)
贴个代码：

```
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
```
