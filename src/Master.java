import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master<Q, R> {
	// �������
	protected Queue<Q> workQueue = new ConcurrentLinkedQueue<Q>();
	// Worker���̶���
	protected Map<String, Thread> threadMap = new HashMap<String, Thread>();
	// ������������
//	protected Map<Thread, R> resultMap = new ConcurrentHashMap<Thread, R>();
	
	protected Queue<R> resultQueue = new ConcurrentLinkedQueue<R>();

	// �Ƿ����е������񶼽�����
	public boolean isComplete() {
		for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
			if (entry.getValue().getState() != Thread.State.TERMINATED) {
				return false;
			}

		}
		return true;
	}

	// Master�Ĺ��죬��Ҫһ��Worker�����߼�������ҪWorker��������
	public Master(Worker<Q, R> worker, int countWorker) {

		worker.setWorkQueue(workQueue);
		worker.setResultQueue(resultQueue);
		for (int i = 0; i < countWorker; i++) {
			threadMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
		}

	}

	// �ύһ������
	public void submit(Q job) {
		workQueue.add(job);
	}

	/**
	 * �ύ����ִ��
	 * 
	 * @param job
	 */
	public void submitAndExe(Q job) {
		workQueue.add(job);
		execute();
	}

	// ��������������

	public Queue<R> getResultQueue() {
		return resultQueue;
	}
	
	// ��ʼ�������е�Worker���̣����д���
	public void execute() {
		for (Map.Entry<String, Thread> entry : threadMap.entrySet()) {
			if (entry.getValue().getState().equals(Thread.State.NEW)) { // ִ��submit�����ύ���´������߳�
				entry.getValue().start();
			}
		}
	}

	public void setResultQueue(Queue<R> resultQueue) {
		this.resultQueue = resultQueue;
	}
}
