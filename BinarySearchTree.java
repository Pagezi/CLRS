public  class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {
    private BinaryNode<AnyType> root;
    BinarySearchTree(BinaryNode<AnyType> rt)
    {
        root=rt;
    }
    private boolean contains(AnyType x,BinaryNode<AnyType> t)
    {
        if(t==null)
            return false;
        int compareResult=x.compareTo(t.element);
        if(compareResult<0)
            return contains(x,t.left);
        else if(compareResult>0)
            return contains(x,t.right);
        else
            return true;
    }
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
    public static void main(String[] args) {
        BinarySearchTree<Integer> bin=new BinarySearchTree<>(new BinaryNode<>(6));
        bin.insert(5);
        bin.insert(9);
        bin.insert(10);
        bin.insert(8);
        bin.insert(7);
        bin.insert(5);
        bin.insert(2);
        bin.Delete(bin.root);
        BinaryNode<Integer> ans=bin.successor(bin.root.left.right);
    }
}
