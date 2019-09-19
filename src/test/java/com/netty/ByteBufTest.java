package com.netty;

import io.netty.buffer.*;
import io.netty.util.Recycler;
import org.junit.Test;

/**
 * @author Administrator
 */
public class ByteBufTest {


    @Test
    public void testToByteBuffer() {
        ByteBufAllocator alloc = PooledByteBufAllocator.DEFAULT;

        //tiny规格内存分配 会变成大于等于16的整数倍的数：这里254 会规格化为256
        ByteBuf byteBuf = alloc.directBuffer(254);

        //读写bytebuf
        byteBuf.writeInt(126);
        System.out.println(byteBuf.readInt());

        //很重要，内存释放
        byteBuf.release();
    }

    /**
     * @author Administrator
     */
    public static class MainTest {

        private static Recycler<HandledObject> newRecycler(int max) {
            return new Recycler<HandledObject>(max) {
                @Override
                protected HandledObject newObject(
                        Handle<HandledObject> handle) {
                    return new HandledObject(handle);
                }
            };
        }



        @Test(expected = IllegalStateException.class)
        public void testMultipleRecycle() {
            Recycler<HandledObject> recycler = newRecycler(1024);
            HandledObject object = recycler.get();
            object.recycle();
            object.recycle();
        }

        static final class HandledObject {
            Recycler.Handle<HandledObject> handle;

            HandledObject(Recycler.Handle<HandledObject> handle) {
                this.handle = handle;
            }

            void recycle() {
                handle.recycle(this);
            }
        }
    }
}
