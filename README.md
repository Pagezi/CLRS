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

$
\max Left = \sum\limits_{t = low}^{mid} {{A_t}}  
$
2. 最大子数组在A[mid+1..high]中 

$
\max Right = \sum\limits_{t = mid + 1}^{high} {{A_t}} 
$

3. 最大子数组跨越了中点mid

$
\max Cross = \sum\limits_{t = start}^{mid - 1} {{A_t}}  + {A_{mid}} + \sum\limits_{t = mid + 1}^{end} {{A_t}} 
$
第三种情况求解办法就是从mid点出发，分别向左向右去遍历最大的子数组。  
这个分治算法的递归式，其中Θ(n)就是计算跨越中点的最大子数组的时间复杂度：

$
T\left( n \right) = \left\{ \begin{array}{l}
\Theta (1)\\
2T\left( {n/2} \right) + \Theta (n)
\end{array} \right.
$


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
 1.是A[i…j]的最大子数组  
2.是某个子数组A[m…j+1]，i≤m≤j+1。

其实我们忘了一个假设，那就是当你处理到`Aj+1`
的时候，我们已经知道以Aj
结尾的最大子数组。那么再加上一个Aj+1的时候对比大小就可以求出以Aj+1结尾的最大子数组。

$
su{m_{j + 1}} = \left\{ {\begin{array}{*{20}{l}}
{\sum\limits_{t = m}^j {{A_t}}  + {A_{j + 1}}}&{if\sum\limits_{t = m}^j {{A_t}}  + {A_{j + 1}} > {A_{j + 1}}}\\
{{A_{j + 1}}}&{if\sum\limits_{t = m}^j {{A_t}}  + {A_{j + 1}} < {A_{j + 1}}}
\end{array}} \right.
$
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

$
\left\lfloor {\lg n} \right\rfloor 
$
- 高度为h的堆最多包含的节点数(练习6.3.3)：

$
\left\lceil {\frac{2}{{{2^{h + 1}}}}} \right\rceil 
$
高度为h的节点上运行MAX-HEAPIFY的代价：

$
{\rm O}\left( h \right)
$
那么综上所述BUILD-MAX-HEAP的总代价：

$
\begin{array}{l}
\sum\limits_{h = 0}^{\left\lfloor {\lg n} \right\rfloor } {\left\lceil {\frac{2}{{{2^{h + 1}}}}} \right\rceil } *{\rm O}\left( h \right) = {\rm O}\left( {n*\sum\limits_{h = 0}^{\left\lfloor {\lg n} \right\rfloor } {\frac{h}{{{2^h}}}} } \right) = {\rm O}(n*\sum\limits_{k = 0}^\infty  {h{{(\frac{1}{2})}^h}} ) = O(n)\\
accordance\\
\sum\limits_{k = 0}^\infty  {k{x^k} = \frac{x}{{{{\left( {1 - x} \right)}^2}}}} {\rm{    }\rm{    }\rm{    }}\sum\limits_{k = 0}^\infty  {h{{(\frac{1}{2})}^h} = \frac{{\frac{1}{2}}}{{{{\left( {1 - \frac{1}{2}} \right)}^2}}} = 2} 
\end{array}
$
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
## 第七章 快速排序
快速排序也是运用了分治法的思想。假设对数组A[p..r]进行快速排序。  
- 分：A[p..r]被划分为两个字数组（可能为空）A[p..q-1]  (这个数组里面每一个元素都小于A[q])和A[q+1..r]  (这个数组里面每一个元素都大于A[q])  
- 治：A[p..q-1]和A[q+1..r]也进行快速排序
- 合：因为是原址排序，所以不需要合并。

***原址排序定义***：在排序算法中，如果输入数组中仅有**常数个元素**需要在排序过程中存储在数组之外，则称排序算法是原址的。  （也就是没有额外空间的啦）  
插入排序、堆排序、快速排序等都是原址排序。  
归并排序不是原址的。
#### 快排的关键是划分！
***N.Lomuto版本***

![image](https://wx4.sinaimg.cn/mw1024/ada24d1cly1frb6m1mzuwj20hg0uego8.jpg)
![image](https://wx2.sinaimg.cn/mw1024/ada24d1cly1frb6m1mfe0j20qm0lkdhp.jpg)
其实是维护了四个区域A[p..i]是小于等于x的，A[i+1,j-1]是大于x的，A[j..r-1]是还没有进行分类的。A[r]也就是数组的最后一个是枢纽元，是基于他来比较的。写程序的时候当遇到大于x的，那么j就直接+1，这轮循环就过去了（a操作）。遇到小于等于x的呢就要让i先加1，然后再与j交换位置（b操作）。


```
int PartitionLomuto(vector<int> &A,int p,int r)
{
    int x=A[r];
    int i=p-1;
    for(int j=p;j<=r-1;j++)
    {
        if(A[j]<=x) {
            i++;
            int temp = A[j];
            A[j] = A[i];
            A[i] = temp;
        }
    }
    int temp = A[r];
    A[r] = A[i+1];
    A[i+1] = temp;
    return i+1;
}
```
***Hoare版本***  
这个版本相对上一个版本，效率上比较高，内存局部性比较好，但是容易写错！算法思想就是，首先还是拿最后的元素作为枢纽元x，第二步先从右边找到一个小于等于x的记录下来、再从左边找一个大于x的位置（注意这两个操作都要边界判断）。第三步是对他们进行交换。
![此处输入图片的描述][1]
```
int PartitionHoare(vector<int> &A,int p,int r)
{
    int x=A[r];
    int i=p-1;
    int j=r+1;
    while(true)
    {
        while(A[--j]<=x)
            if(j==p)
                break;
        while(A[++i]>x)
            if(j==r)
                break;
        if(i<j)
        {
            int temp=A[j];
            A[j]=A[i];
            A[i]=temp;
        }
    }
}
```
#### 快排性能分析
***最坏情况划分***
这种情况出现在划分非常不平均的时候，比如说两个子问题分别包含n-1和0个元素的时候。划分操作时间复杂度Θ(n)。

$
\begin{array}{l}
T(n) = T(n - 1) + T(0) + \Theta (n) = T(n - 1) + \Theta (n)\\
\sum\limits_{k = 1}^n k  = 1 + 2 + ... + n = \frac{1}{2}*n*(n + 1)\\
T(n) = \Theta (1) + \Theta (2) + ... + \Theta (n) = \Theta ({n^2})
\end{array}
$
如果序列基本完全有序了，快排时间复杂度Θ(n^2),而插入排序是Θ(n)。所以说，快排并不是在任何时候都是比插入排序快的！  
***最好情况划分***
就是每次子数组都是相对平均划分的时候啦。直接列出递归式，主定理的情况2可以求出Θ(nlgn)

$
T(n) = 2T(n/2) + \Theta (n)
$
***平衡的划分***  
快速排序的平均运行时间更加接近于最好情况。
我们看下面的递归树，每次乘1/10的肯定更快到1，每次乘9/10的肯定更慢到1。所以这样就会造成有一边后来就没有叶子节点的，后面几层的代价就会小于等于cn。树的深度肯定是为Θ(lgn)！！其实99：1的划分也是Θ(lgn)！！任何常熟比例的划分都会产生深度为

$
T(n) = T(\frac{9}{{10}}n) + T(\frac{1}{{10}}n) + cn
$

![image](https://wx4.sinaimg.cn/mw1024/ada24d1cly1frb8pr15utj20ve0lcmz9.jpg)
![image](https://wx2.sinaimg.cn/mw1024/ada24d1cly1frb8pqzz7aj213g0a0myd.jpg)
## 第八章 线性时间排序
#### 证明基于比较的排序下届是Ω(nlgn)
![image](https://wx3.sinaimg.cn/mw1024/ada24d1cly1fr8jln1qr8j20my0bmwfl.jpg)
这是一颗决策树模型。每个内部节点都用i:j标记，他比较的是A[i]<=A[j]。叶子节点上是一个序列，表示的是一种排列方式。加了阴影的路径是输入序列<a1=6,a2=8,a3=5>进行排序的时候做的决策。n个元素有n!种可能的排列都应该出现在决策树的叶子节点。 
一个基于比较的排序最坏情况就是比较次数等于决策树的高度。假设一个高度为h，具有l个可达节点的决策树。
$
\begin{array}{l}
n! \le l \le {2^h}{\rm{ }}\\
h \ge \lg (n!) = \Omega (n\lg n)\\
\lg (n!) = \lg 1 + \lg 2 +  \ldots  \ldots  + \lg n \le n\lg n = {\rm O}(n\lg n)
\end{array}
$
#### 计数排序
n个输入元素中每一个都是0到k之间的整数，当k=O(n)时，排序的运行时间为Θ(n)。计数排序的思想是：对每一个输入元素x，确定小于x的元素个数，从而确定元素应该放到哪个位置。实际上就是确定三个数组，输入输出数组A[1..n],B[1..n]。C作为临时技术的数组C[0..k]。分为三步走
1. 先按计数方法把A中每个元素个数统计到C相应的下标下。比如A中0有两个，C[0]为2。
2. C中累加。C[i]=C[i]+C[i-1]
3. 从A的末尾开始。A的值去C相应下标寻找，找到的就是应该放到B的位置。例如A[8]中的3在C中找到的是7，那么就放在B的7位置。相应的C[3]要减一。  
  
但是如果k=Ω(nlgn)的话还不如基于比较的排序呢。也就是说n比较小k比较大，n=100，k=10000这种情况吧。

还有一点这个排序算法是具有**稳定性**的。即相同值在输出数组中的相对次序与输入数组中的相对次序相同。
![image](https://wx4.sinaimg.cn/mw1024/ada24d1cly1fr8wnssabyj217y0j4411.jpg)
```
#include <iostream>
#include <vector>
using namespace std;

vector<int> CountSort(vector<int> A,int k)
{
    int n=A.size();
    vector<int> B(n,-1);
    vector<int> C(k,0);
    for(int i=0;i<n;i++)
        C[A[i]]++;
    for(int i=1;i<k;i++)
        C[i]=C[i]+C[i-1];
    for(int i=n-1;i>=0;i--)
    {
        B[C[A[i]]-1]=A[i];//B下标从0开始记
        C[A[i]]--;
    }
    return B;
}

```
## 第十一章 散列表
散列表是实现**字典结构**的一种有效的数据结构。散列表查找一个元素的平均时间是O(1),虽然最坏情况下查找一个元素的时间和链表相同Θ(n)，但是实际应用中，挺好的。
### 直接寻址表
说白了就是数组啦。当关键字的全域U（关键字的范围）比较小时，直接寻址是一种简单而有效的技术。我们假设某应用要用到一个动态集合，其中每个元素的关键字都是取自于全域`U=｛0，1，…，m-1｝`，其中m不是一个很大的数。另外，假设每个元素的关键字都不同。  
![image](http://wx3.sinaimg.cn/mw690/ada24d1cgy1fruxxbgdfmj20v20f6goa.jpg)
为表示动态集合，我们用一个数组，或称为直接寻址表（direct-address table），记为`T[0~m-1]`，其中每一个位置（slot，槽）对应全域U中的一个关键字，对应规则是，槽k指向集合中关键字为k的元素，如果集合中没有关键字为k的元素，则`T[k]=NIL`。
### 散列表
直接寻址表缺点很明显：；对于全域较大，但是元素却十分稀疏的情况，使用这种存储方式将浪费大量的存储空间。  
为了解决这种情况，我们利用散列函数来计算关键字k所在的位置。简单的说就是讲范围较大的关键字映射到一个范围较小的集合中。这时我们可以说，一个具有关键字k的元素被散列到槽h(k)上，或者说h(k)是关键字k的散列值。  
![image](http://wx4.sinaimg.cn/mw690/ada24d1cgy1fruy4wqs76j20ru0fuwgs.jpg)
时会产生一个问题：两个关键字可能映射到同一槽中（我们称之为冲突（collision）），并且不管你如何优化h(k)函数，这种情况都会发生，因为只有m个槽（|U|>m）。所以我们只需要做两点内容：1.处理冲突。2.尽量减少冲突
### 处理冲突：结链法
结链法的**插入操作**很简单，只需要`O(1)`的时间，用链表的头插法。**删除操作**如果是双链表就是`O(1)`，如果是单链表那就是和查找操作的渐近时间相同。  

下面重点来说**查找操作**。一个散列表T，有m个槽位，放入n个元素。定义T的装填因子为$α=n/m$。这里先引入一个假设叫==简单均匀散列==。也就是说任何一个给定元素等可能地散列到m个槽中的任何一个，且与其他元素被散列到什么位置无关。一般都是在这个前提下进行讨论的。每一个链上的平均个数其实就是装填因子α




***定理11.1 在简单均匀散列假设下，用结链法解决冲突，一次不成功查找的平均时间为Θ(1+α)***  
查找一个关键字k，在不成功的情况下，查找的期望时间就是查找到链表末尾的期望时间，所以一次不成功需要检查α个元素。并且所需要的总时间是$Θ(1+α)$，其中计算h(k)的时间为O(1)。

***定理11.2 在简单均匀散列假设下，用结链法解决冲突，一次成功查找的平均时间为Θ(1+α)***  
在一次成功查找中，所检查的元素是成功元素x所在链表中前面元素多1(多1是因为x自己本身嘛)。因为链表是头插法，那么链表里x之前的元素其实是在x之后插入的。设Xi为插入到表中的第i个元素，设ki=xi.key。定义指示器随机变量$Xij=I{h(ki)=h(kj)}。P(h(ki)=h(kj))=1/m$,推出E[Xij]=1/m。在一次成功查找中，所检查的元素期望数目：

$
\begin{array}{l}
{X_{ij}} = \left\{ \begin{array}{l}
0 & h\left( {{k_i} \ne {k_j}} \right)\\
1 & h\left( {{k_i} = {k_j}} \right)
\end{array} \right.\\
E[\frac{1}{n}\sum\limits_{i = 1}^n {(1 + \sum\limits_{j = i + 1}^n {{X_{ij}}} } )] = \frac{1}{n}\sum\limits_{i = 1}^n {(1 + \sum\limits_{j = i + 1}^n {E[{X_{ij}}} } ])\\
 = \frac{1}{n}\sum\limits_{i = 1}^n {(1 + \sum\limits_{j = i + 1}^n {\frac{1}{m}} } ) = 1 + \frac{1}{{nm}}\sum\limits_{i = 1}^n {(n - i)} \\
 = 1 + \frac{1}{{nm}}({n^2} - \frac{{n(n + 1)}}{2}) = 1 + \frac{{n - 1}}{{2m}} = 1 + \frac{\alpha }{2} - \frac{\alpha }{{2n}}
\end{array}
$
j从i之后开始找，如果是相同链的，就为1，否则就为0
### 处理冲突：开放寻址法
开放寻址法中，所以元素都放在散列表里面。也就是说，每个槽位要么就是动态集合里面的一个元素，要么就是NIL。计元素个数n,散列表槽位m，该方法$n \le m$。装填因子$α$不会超过1。

使用开放寻址表连续插入或者查找一个元素，需要连续的检查散列表，直到找到一个空槽为止，这种称为**探查(probe)**。所以我们可以定义一个探查序列,第二个参数为探查�号，如下所示：$\left\langle {h(k,0),h(k,1),...,h(k,m - 1)} \right\rangle$

插入比较好理解，只需要根据散列函数的计算不断的找到一个为NIL的位置，然后进行插入。查找也是同样，根据散列函数如果找到了就返回，直到找到一个NIL位置就查找失败。删除比较麻烦，不能直接让这个位置为NIL，因为这样可能会导致NIL后面的元素查找失败。这时候可以在槽内建立一个DELETED标识，删除的时候把这个标识置为true，查找的时候就会绕过这个槽位，查找下一个。***在实际应用中，如果集合要用到删除操作，一般是用结链法解决冲突的。***
#### 线性探查法
$h\left( {k,i} \right) = (h'(k) + i)\bmod {\rm{ }}m,i = 0,1,...,m - 1$
给定一个关键字，首先探查槽$T[h'(k)]$,再探查$T[h'(k)+1]$，依次类推直到$T[m-1]$。然后像循环队列一样又从$T[0],T[1]$,开始探查。在这种方法下初始探查位置决定了整个序列，所以有m种不同的探查序列。这种探查方法会遇到一个问题就是**一次群集**，随着连续的槽不断被占用，平均查找时间增加。当空槽前有i个满的槽时，该空槽下一个被占用的概率是$(i+1)/m$。
![此处输入图片的描述][2]
#### 二次探查法
$
h(k,i) = (h'(k) + {c_1}i + {c_2}{i^2})\bmod {\rm{ m}},{\rm{ }}i = 0,1,...,m - 1
$
和线性探查法一样，初始探查位置决定了整个序列，所以有m种不同的探查序列。而且这种探查方法初始探查位置相同，他们的探查序列也是相同的。$h(k1,0)=h(k2,0)$蕴含着$h(k1,i)=h(k2,i)$但是这种探查法只会导致轻度的**二次集群**。这种探查法依赖于$c1,c2,m$的精确选择。
#### 双重散列法

### 散列函数
#### 除法散列法

$
h(k) = k\bmod {\rm{ }}m
$
关键是m值得选取，m应该是一个不太接近2的整数幂的素数。m最不应该是二的整数幂。例如：m = 8 = 2^3 。那么$1456 Mod 8 = 2456 Mod 8 = 3456 Mod 8 $…………如果这样的话，那么H(k)重复得概率就会大大的增加了。也可以理解为如下图，计算机在计算的时候以二进制进行。m取2的整数幂其实就是取k的最低位p位的数字。
![此处输入图片的描述][3]
#### 乘法散列法
$
\begin{array}{l}
h(k) = \left\lfloor {m(kA\bmod {\rm{ }}1)} \right\rfloor  = \left\lfloor {{2^p}(\frac{{s*k}}{{{2^w}}}\bmod {\rm{ }}1)} \right\rfloor \\
0 < s < {2^w}
\end{array}
$
这个散列法优点是对m的选择不是特别关键，一般选择为2的某次整数幂。
 1. 关键字k乘以常数A(A在0到1之间),并且提取A的小数部分。 
 2. 用这个小数部分乘以m，并且向下取整
w表示计算机的位，32位还是64位。k正好是用一个字可以表示的数字。
计算机里除2就是右移操作，乘2就是左移操作。k*s可以看做是一个2w位的值。
具体做法如下两步：

![此处输入图片的描述][4]






## 第十二章 二叉查找树（BST）
集合的操作：查找、最小元、最大元、上一元素、下一元素、插入、删除。这些操作二叉查找树全部都支持！而且二叉查找树上的基本操作所花费的时间与这棵树的高度成正比。但是树的高度是在lgn到n之间的。因此才会出现之后诸如红黑树之类的能保证树高是O(lgn)的数据结构。
```
    private static class BinaryNode<AnyType>
    {
        AnyType element;
        BinaryNode<AnyType> left;
        BinaryNode<AnyType> right;
        BinaryNode<AnyType> parent;

        BinaryNode(AnyType theElement)
        {
            this(theElement,null,null,null);
        }
        BinaryNode(AnyType theElement,BinaryNode<AnyType> lt,BinaryNode<AnyType> rt,BinaryNode<AnyType> pa)
        {
            element=theElement;
            left=lt;
            right=rt;
            parent=pa;
        }
    }
```

#### 性质
*设x是二叉搜索树的一个节点，如果y是x的左子树中的一个节点，那个y.key<=x.key。如果y是x的右子树中的一个节点，那个y.key>x.key。
![image](http://wx1.sinaimg.cn/large/ada24d1cgy1frki42xw1cj20kc08pt8x.jpg)
一般二叉查找树经过**中序遍历**以后可以得到一个有序的数列，且需要耗费Θ(n)。证明见原书P162页，反正没有看懂。
#### 查找操作
查找操作很简单，就是当前元素大于要查找的元素那么久往左边子树去找。否则就去右边子树找。
#### 最大最小元操作
这个也很好理解，最小元就是一直left...left。最大元就是一直right...right。下面展示了递归和非递归的两种写法

```
    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> t)
    {
        if(t==null)
            return null;
        else if(t.left==null)
            return t;
        return findMin(t.left);
    }
    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> t)
    {
        if(t!=null)
        {
            while(t.right!=null)
                t=t.right;
        }
        return t;
    }
```

#### 后继和前驱操作
找x的下一个元素y有两种情况：
1. 右孩子不空，那么就找右子树的最小元，也就是x的右孩子为基点一直left...left..left
1. 右孩子为空，向上找到第一个左分支（如下图）  
![image](http://wx3.sinaimg.cn/mw690/ada24d1cgy1frkkdb8v5bj20b208t0vr.jpg)

找x的上一个元素y就是相反咯：
3. 左孩子不空，那么就找左子树的最大元，也就是x的左孩子为基点一直right...right。
4. 左孩子为空，向上找到第一个右分支  
仔细观察会发现上面的2,3和1,4就像是两个互逆的过程。

```
    private BinaryNode<AnyType> successor(BinaryNode<AnyType> x)
    {
        if(x.right!=null)
            return findMin(x.right);
        BinaryNode<AnyType> y=x.parent;
        while (y!=null&&x==y.right)
        {
            x=y;
            y=y.parent;
        }
        return y;
    }
    private BinaryNode<AnyType> predecessor(BinaryNode<AnyType> x)
    {
        if(x.left!=null)
            return findMax(x.left);
        BinaryNode<AnyType> y=x.parent;
        while (y!=null&&x==y.left)
        {
            x=y;
            y=y.parent;
        }
        return y;
    }
```

#### 插入操作

```
    private void insert(AnyType z)
    {
        int compareResult;
        BinaryNode<AnyType> y=null;
        BinaryNode<AnyType> x=this.root;
        if(x==null) // 如果这个二叉查找树为空，就直接插入作为根节点
        {
            this.root=new BinaryNode<>(z);
            return;
        }
        //寻找到y，这个y就是记录要插入位置的父亲节点处
        while (x!=null)
        {
            y=x;
            compareResult=z.compareTo(x.element);
            if(compareResult<0)
                x=x.left;
            else
                x=x.right;
        }
        //寻找要插入右还是左边
        compareResult=z.compareTo(y.element);
        if(compareResult<0)
            y.left=new BinaryNode<>(z,null,null,y);
        else
            y.right=new BinaryNode<>(z,null,null,y);
    }
```

#### 删除
删除操作一般分为以下几种情况，假设删除节点z：
1. 没孩子，那么这个就是叶子节点，直接删除就好了，但是要记住修改父节点的指针指向
2. 只有一个孩子，直接用这个孩子上升替换。
![image](http://wx1.sinaimg.cn/mw690/ada24d1cgy1frkla7cospj20fo0a5glr.jpg)
3. 两个孩子的情况比较复杂分为以下两种情况：
- 右孩子的左孩子为空
![image](http://wx3.sinaimg.cn/mw690/ada24d1cgy1frkla7tjihj20et068q30.jpg)
- 右孩子有左孩子
![image](http://wx2.sinaimg.cn/mw690/ada24d1cgy1frkla8kpyxj20n807zt93.jpg)

```
    //用一颗以v为根的子树来替换以u为根的子树
    private void Transplant(BinaryNode<AnyType> u,BinaryNode<AnyType> v)
    {
        //如果u的父亲节点直接是NULL，那么代表u就是这个树的根
        if(u.parent==null)
            this.root=v;
        else if(u==u.parent.left)//u是他父亲节点的左孩子
            u.parent.left=v;
        else
            u.parent.right=v;
        if(v!=null)
            v.parent=u.parent;
    }
    private void Delete(BinaryNode<AnyType> z)
    {
        if(z.left==null)
            Transplant(z,z.right);
        else if(z.right==null)
            Transplant(z,z.left);
        else
        {
            BinaryNode<AnyType> y=findMin(z.right);
            if(y.parent!=z)//这种就是右孩子有左孩子的情况，y先用x替换
            {
                Transplant(y,y.right);
                y.right=z.right;
                y.right.parent=z;
            }
            Transplant(z,y);//z用y替换
            y.left=z.left;
            y.left.parent=y;
        }
    }
```
## 第十三章 红黑树
上一章介绍的二叉查找树每次操作最坏的时间复杂度是O(h),树的高度比较低，这些操作会执行的比较快。这回介绍的红黑树就是一种保证树的高度比较小的一种平衡搜索树。时间复杂度为O(lgn)
### 性质
1. 每个节点非红即黑
2. 根节点是黑色的
3. 没个叶子节点（NIL）是黑色的
4. 如果一个节点是红色的，他的孩子一定为黑色
5. 每个节点，到其后代的所有叶子节点的简单路径上，均包含相同的黑色节点。 

**注意：**
- 特性(3)中的叶子节点，是只为空(NIL或null)的节点，除去这些NIL节点，其他节点叫做内节点。
- 特性(5)，确保没有一条路径会比其他路径长出俩倍。因而，红黑树是相对是接近平衡的二叉树。
![image](http://wx3.sinaimg.cn/mw690/ada24d1cly1froot6b1suj20ym0y4wl4.jpg)
### 证明O(lgn)
**黑阶bh(x)：**  
从某个节点x出发（不包括该节点）到达一个叶节点的任意一条路径上，黑色节点的个数称为该节点的黑阶。

**关于这个性质我们需要注意两点**：  
-  第1点：根据红黑树的"特性(5)，即从一个节点到该节点的子孙节点的所有路径上包含相同数目的黑节点"可知，从节点x出发到达的所有的叶节点具有相同数目的黑节点。这也就意味着，bh(x)的值是唯一的！
-   第2点：根据红黑色的"特性(4)，即如果一个节点是红色的，则它的子节点必须是黑色的"可知，从节点x出发达到叶节点"所经历的黑节点数目">= "所经历的红节点的数目"。假设x是根节点，则可以得出结论"bh(x) >= h/2"。  
#### 证明：一棵含有n个节点的红黑树的高度至多为2log(n+1).

$
\begin{array}{l}
h \le 2\log (n + 1)\\
\frac{h}{2} \le \log (n + 1)\\
{2^{{\textstyle{h \over 2}}}} \le n + 1{\rm{ }} \Rightarrow {\rm{n}} \ge {2^{{\textstyle{h \over 2}}}} - 1\\
\left\{ \begin{array}{l}
{\rm{n}} \ge {2^{{\textstyle{h \over 2}}}} - 1\\
bh(x) \ge \frac{h}{2}
\end{array} \right. \Rightarrow {\rm{n}} \ge {2^{bh(x)}} - 1
\end{array}
$
转换一下只要证明内节点个数为2^bh(x)-1个！  
下面通过"数学归纳法"开始论证：  
1. 当树的高度h=0时，内节点个数是0，bh(x) 为0，2bh(x)-1 也为 0。显然，原命题成立。
2. 考虑一个h>0，有两个子节点的内部节点x。如果子节点为红那么这个字节点黑阶为b(x)，否则为b(x)-1。因为x的子节点本身比x本身高度要低，所以每个子节点至少有2^(b(x)-1)-1个内部节点
 ![image](http://wx2.sinaimg.cn/mw690/ada24d1cly1froq3kcok0j21c20einls.jpg)
### 旋转
无论是左旋还是右旋，被旋转的树，在旋转前是二叉查找树，并且旋转之后仍然是一颗二叉查找树。  
左旋中的“左”，意味着“被旋转的节点将变成一个左节点”  
右旋中的“右”，意味着“被旋转的节点将变成一个右节点”。
```
                               z
   x                          /                  
  / \      --(左旋)-->       x
 y   z                      /
                           y
                           
                           
                               y
   x                            \                 
  / \      --(右旋)-->           x
 y   z                            \
                                   z
```

![image](http://wx1.sinaimg.cn/mw690/ada24d1cgy1froqrhoazwj20ic0603z5.jpg)
![image](http://wx4.sinaimg.cn/mw690/ada24d1cgy1frorjhfiigj21kw0mrdpp.jpg)
```
RIGHT-ROTATE(T,y):
x=y.left
y.left=x.right     // 将 “x的右孩子” 设为 “y的左孩子”，即 将β设为y的左孩子
x.right.parent=y   // 将 “y” 设为 “x的右孩子的父亲”，即 将β的父亲设为y
x.parent=y.parent   // 将 “y的父亲” 设为 “x的父亲”
if(y.parent==T.nil)
	T.root=x             // 情况1：如果 “y的父亲” 是空节点，则将x设为根节点
else if y=y.parent.right
	y.parent.right=x      // 情况2：如果 y是它父节点的右孩子，则将x设为“y的父节点的左孩子”
else
	y.parent.left=x       // 情况3：(y是它父节点的左孩子) 将x设为“y的父节点的左孩子”
x.right=y
y.parent=x
```
### 插入
将一个节点插入到红黑树中，需要执行哪些步骤呢？首先，将红黑树当作一颗二叉查找树，将节点插入；然后，将节点着色为红色；最后，通过旋转和重新着色等方法来修正该树，使之重新成为一颗红黑树。详细处理过程描述如下：

- **第一步: 将红黑树当作一颗二叉查找树，将节点插入。**  
红黑树本身就是一颗二叉查找树，将节点插入后，该树仍然是一颗二叉查找树。也就意味着，树的键值仍然是有序的。此外，无论是左旋还是右旋，若旋转之前这棵树是二叉查找树，旋转之后它一定还是二叉查找树。这也就意味着，任何的旋转和重新着色操作，都不会改变它仍然是一颗二叉查找树的事实。

- **第二步：将插入的节点着色为"红色"。**  
为什么着色成红色，而不是黑色呢？因为将插入的节点着色为红色，不会违背"特性(5)"！少违背一条特性，就意味着我们需要处理的情况越少。接下来，就要努力的让这棵树满足其它性质即可；满足了的话，它就又是一颗红黑树了。

- **第三步: 通过一系列的旋转或着色等操作，使之重新成为一颗红黑树。**  
       对于"特性(2)"，显然也不会违背。在第一步中，我们是将红黑树当作二叉查找树，然后执行的插入操作。而根据二叉查找数的特点，插入操作不会改变根节点。所以，根节点仍然是黑色。若是空树，直接判断，改变颜色即可  
       对于"特性(4)"，是有可能违背的！而且最难处理的就是这个性质
       那接下来，想办法使之"满足特性(4)"，就可以将树重新构造成红黑树了。

#### 维护红黑树特性分为三种情况：  
**1.当前节点的父节点是红色，且当前节点的祖父节点的另一个子节点（叔叔节点）也是红色。**  
- 将“父节点”设为黑色。
- 将“叔叔节点”设为黑色。
- 将“祖父节点”设为“红色”。
- 将“祖父节点”设为“当前节点”(红色节点)；即，之后继续对“当前节点”进行操作。   
![image](http://wx2.sinaimg.cn/mw690/ada24d1cgy1frosl9if6rj20x40dw767.jpg)

这样操作的意义：父节点和当前节点都为红色，违背了特性(4)。但是直接把父节点变为黑色又违背了特性(5),因为**包含“父节点”的分支的黑色节点的总数增加了1**。这个时候只需要把叔叔节点变成黑色，那不是就解决问题了？还不行！++我们可以得知祖父节点肯定是黑色的++。那么会导致**包含祖父节点的分支的黑色节点的总数增加了1的问题**。既然这样，我们通过将“祖父节点”由“黑色”变成“红色”       

按照上面的步骤处理之后：当前节点、父节点、叔叔节点之间都不会违背红黑树特性，但祖父节点却不一定。若此时，祖父节点是根节点，直接将祖父节点设为“黑色”，那就完全解决这个问题了；若祖父节点不是根节点，那我们需要将“祖父节点”设为“新的当前节点”，接着对“新的当前节点”进行分析。
++note:如果祖父节点是红色，那么他的孩子必须为黑色，那么之前红黑树的性质就不满足了。++

**2.当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的右孩子**
- 将“父节点”作为“新的当前节点”。
- 以“新的当前节点”为支点进行左旋。
![image](http://wx1.sinaimg.cn/mw690/ada24d1cgy1frot3g3l18j20rq080q3y.jpg)

为了便于说明，我们设置“父节点”的代号为F(Father)，“当前节点”的代号为S(Son)，“祖父节点”的代号为G(Grand-Father)
为什么要“以F为支点进行左旋”呢？==这里左旋只是为了到达Case 3的效果，为Case 3的右旋做准备==。而在Case 2中，只通过color changes操作没法保证避免性质4、5.这时候我们只能迫于无奈采用旋转操作。假设直接采用case3的右旋转操作。变F为黑色，但是此时F所在路径多了一个黑节点，我们想把F往上一层，然后把G变为红色右侧下一层，也就是所谓的对G进行右旋转。注意到，G此时变为红色，而且F的右孩子也就是S（红色）要变成G的左孩子。那么变化后，G和F在相邻的两层了，而且同时为红色。又带来了冲突。最简单的方法就是在右旋转的前，把当前节点红色转移到左孩子去。

**3.当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的左孩子**
- 将“父节点”设为“黑色”。
- 将“祖父节点”设为“红色”。
- 以“祖父节点”为支点进行右旋。
- 
![image](http://wx1.sinaimg.cn/mw690/ada24d1cgy1frot3g3l18j20rq080q3y.jpg)

为了便于说明，我们设置“当前节点”为S(Original Son)，“父节点”为F(Father)，祖父节点为G(Grand-Father)。
      S和F都是红色，违背了红黑树的“特性(4)”，我们可以将F由“红色”变为“黑色”，就解决了“违背‘特性(4)’”的问题；但却引起了其它问题：违背特性(5)，因为将F由红色改为黑色之后，所有经过F的分支的黑色节点的个数增加了1。那我们如何解决“所有经过F的分支的黑色节点的个数增加了1”的问题呢？ 我们可以通过“将G由黑色变成红色”，同时“以G为支点进行右旋”来解决。
插入伪代码：
```
RB-INSERT(T,z)
y=T.nil
x=T.root
while x!=T.nil  //寻找到要插入的位置的父节点
	y=x
	if(z.key<x.key)
		x=x.left
	else
		x=x.right
z.p=y //插入过程，对位置上的父节点做三种判断
if(y==T.nil)
	T.root=z
else if(z.key<y.key)
	y.left=z
else
 	y.right=z
 z.left=z.right=T.nil //插入的位置肯定是叶子节点
 z.color=RED
 RB-INSERT-FIXUP(T,z)
```
维持红黑树特性：
```
RB-INSERT-FIXUP(T,z)
while z.p.color==RED
	if(z.p==z.p.p.left)
		y=z.p.p.right    //叔节点
		if(y.color==RED)    //处理第一种情况
			z.p.color=BLACK
			y.color=BLACK
			z.p.p.color=RED
			z=z.p.p
		else if(z==z.p.right)  //处理第二种情况
			z=z.p
			LEFT-ROTATE(T,z)
		z.p.color=BLACK       //处理第三种情况
		z.p.p.color=RED
		RIGHT-ROTATE(T,z.p.p)
	else if(z.p==z.p.p.right)
		y=z.p.p.left    //叔节点
		(后面类似。。。)
```
![image](http://wx4.sinaimg.cn/mw690/ada24d1cgy1frot3jxpn4j20qy0wcadc.jpg)
### 删除


  [1]: https://wx4.sinaimg.cn/mw1024/ada24d1cly1frb7f29idnj210y0kuq71.jpg
  [2]: http://wx3.sinaimg.cn/mw690/ada24d1cgy1frvsq2nikxj20h20bgjsq.jpg
  [3]: http://wx2.sinaimg.cn/mw690/ada24d1cgy1frvopmgkrrj208q04tjrq.jpg
  [4]: http://wx3.sinaimg.cn/mw690/ada24d1cgy1frvqnjeadkj20wg0egwgj.jpg