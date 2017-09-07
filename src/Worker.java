import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker<Q,R> implements Runnable{  
  
    //任务队列，用于取得子任务  
    protected Queue<Q> workQueue;  
    //子任务处理结果集  
    protected Queue<R> resultQueue = new ConcurrentLinkedQueue<R>();
    
    public void setWorkQueue(Queue<Q> workQueue){  
        this.workQueue= workQueue;  
    }  
      
    public void setResultQueue(Queue<R> resultQueue) {
		this.resultQueue = resultQueue;
	}

    //子任务处理的逻辑，在子类中实现具体逻辑  
    public R handle(Q input){  
        return (R) input;  
    }  
      
      
    public void run() {  
          
        while(true){  
            //获取子任务  
            Q input= workQueue.poll();  
            if(input==null){  
                break;  
            }  
            //处理子任务  
            R re = handle(input);  
            resultQueue.add(re);
        }  
    }  

}
