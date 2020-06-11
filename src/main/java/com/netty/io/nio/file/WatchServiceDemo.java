package com.netty.io.nio.file;

import java.io.IOException;
import java.nio.file.*;

/**
 *
 * @author ex-pengfx
 */
public class WatchServiceDemo {
    public void watchDir(Path path) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);

        while (true){
            WatchKey take = watchService.take();
            for(WatchEvent<?> watchEvent:take.pollEvents()){
                WatchEvent.Kind<?> kind = watchEvent.kind();

                if(kind==StandardWatchEventKinds.OVERFLOW){
                    continue;
                }

                WatchEvent<Path> event = (WatchEvent<Path>) watchEvent;
                Path context = event.context();
                System.out.println(kind+"："+context);
            }

            boolean reset = take.reset();

            //如果文件或目录被删除，则退出
            if(!reset){
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Path path =Paths.get("D:\\");
        WatchServiceDemo watch = new WatchServiceDemo();
        watch.watchDir(path);
    }
}
