import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master<Q, R> {
	// 任务队列
	protected Queue<Q> workQueue = new ConcurrentLinkedQueue<Q>();
	// Worker进程队列
	protected Map<String, Thread> threadMap = new HashMap<String, Thread>();
	// 子任务处理结果集
//	protected Map<Thread, R> resultMap = new ConcurrentHashMap<Thread, R>();
	
	protected Queue<R> resultQueue = new ConcurrentLinkedQueue<R>();

	// 是否所有的子任务都结束了
	public boolean isComplete() {
		for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
			if (entry.getValue().getState() != Thread.State.TERMINATED) {
				return false;
			}

		}
		return true;
	}

	// Master的构造，需要一个Worker进程逻辑，和需要Worker进程数量
	public Master(Worker<Q, R> worker, int countWorker) {

		worker.setWorkQueue(workQueue);
		worker.setResultQueue(resultQueue);
		for (int i = 0; i < countWorker; i++) {
			threadMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
		}

	}

	// 提交一个任务
	public void submit(Q job) {
		workQueue.add(job);
	}

	/**
	 * 提交并且执行
	 * 
	 * @param job
	 */
	public void submitAndExe(Q job) {
		workQueue.add(job);
		execute();
	}

	// 返回子任务结果集

	public Queue<R> getResultQueue() {
		return resultQueue;
	}
	
	// 开始运行所有的Worker进程，进行处理
	public void execute() {
		for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
			if (entry.getValue().getState().equals(Thread.State.NEW)) { // 执行submit方法提交的新创建的线程
				entry.getValue().start();
			}
		}
	}

	public void setResultQueue(Queue<R> resultQueue) {
		this.resultQueue = resultQueue;
	}
}
