import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker<Q,R> implements Runnable{  
  
    //������У�����ȡ��������  
    protected Queue<Q> workQueue;  
    //������������  
    protected Queue<R> resultQueue = new ConcurrentLinkedQueue<R>();
    
    public void setWorkQueue(Queue<Q> workQueue){  
        this.workQueue= workQueue;  
    }  
      
    public void setResultQueue(Queue<R> resultQueue) {
		this.resultQueue = resultQueue;
	}

    //����������߼�����������ʵ�־����߼�  
    public R handle(Q input){  
        return (R) input;  
    }  
      
      
    public void run() {  
          
        while(true){  
            //��ȡ������  
            Q input= workQueue.poll();  
            if(input==null){  
                break;  
            }  
            //����������  
            R re = handle(input);  
            resultQueue.add(re);
        }  
    }  

}
