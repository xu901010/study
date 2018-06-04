package cn.com.xyc.study.socket.nio.test;

import java.nio.IntBuffer;

public class TestBuffer {
    public static void main(String[] args) {
        //创建指定长度的缓冲区
        IntBuffer buf = IntBuffer.allocate(10);
        //position位置：0 - > 1
        buf.put(13);
        //position位置：1 - > 2
        buf.put(21);
        //position位置：2 - > 3
        buf.put(35);
        //把位置复位为0，也就是position位置：3->0
        buf.flip();
        System.out.println("使用flip复位：" + buf);
        //容量一量初始化后不允许改变(warp方法包裹数组除外)
        System.out.println("容量为： " + buf.capacity());
        //由于只装载了三个元素，所以可读取或者操作的元素为3 则Limit=3
        System.out.println("限制为：" + buf.limit());

        System.out.println("获取下标为1的元素：" + buf.get(1));
        System.out.println("get(index)方法，position位置不改变： " + buf);
        buf.put(1, 4);
        System.out.println("put(index,change)方法，position位置不变：" + buf);

        for (int i = 0; i < buf.limit(); i++) {
            //调用get方法会使其缓冲区位置(position)向后递增一位
            System.out.println(buf.get() + "\t");
        }
        System.out.println("buf对象遍历之后为：" + buf);


        int[] arr = new int[]{1, 2, 5};
        IntBuffer buf1 = IntBuffer.wrap(arr);
        System.out.println(buf1);

        IntBuffer buf2 = IntBuffer.wrap(arr, 0, 2);
        //这样使用表示容量为数组arr的长度，但是可操作的元素只有实际进入缓存区的元素长度
        System.out.println(buf2);

        IntBuffer buf3 = IntBuffer.allocate(10);
        int[] arr1 = new int[]{1, 2, 5};
        buf3.put(arr1);
        System.out.println(buf3);
        //一种复制方法
        IntBuffer buf4 = buf3.duplicate();
        System.out.println(buf4);

        //设置buf3的位置属性
        buf3.position(1);
        System.out.println(buf3);
        System.out.println("可读数据为：" + buf3.remaining());

        int[] arr3 = new int[buf3.remaining()];
        //将缓冲区数据放入arr3数组中去
        buf3.get(arr3);
        for (int i : arr3) {
            System.out.print(Integer.toString(i) + ",");
        }
    }
}
