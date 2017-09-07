import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class MemoryGroupUtils {

	private static Collection<Three> _groupBy(List<? extends Map<String, Object>> lists, String... field) {
		Map<String, Three> rltMap = new HashMap<String, Three>();
		for (Map map : lists) {
			StringBuilder groupByKey = new StringBuilder("");
			for (String s : field) {
				groupByKey.append(map.get(s));
			}
			Three<Object, List, Object> threed = rltMap.get(groupByKey.toString());
			if (threed != null) {// 之前已经有重复的值了，直接放进去即可
				threed.getS().add(map);
				continue;
			}
			// 之前没有重复的值，new一个出来
			Three<Object, List, Object> three = new Three<Object, List, Object>(groupByKey, new LinkedList<>(),
					new Object());
			rltMap.put(groupByKey.toString(), three);
		}
		return rltMap.values();
	}

	public static List<Three> groupBy(List<Map<String, Object>> lists, final String... field) {
		if (lists.size() > 100000) {
			List<List<Map<String, Object>>> rlList = shareByFixedNum(lists, 5);
			Master<List<Map<String, Object>>, Collection<Three>> master = new Master<List<Map<String, Object>>, Collection<Three>>(
					new Worker<List<Map<String, Object>>, Collection<Three>>() {
						public Collection<Three> handle(List<Map<String, Object>> input) {
							Collection<Three> list = _groupBy(input, field);
							return list;
						}
					}, 5);

			for (List<Map<String, Object>> list : rlList) {
				master.submitAndExe(list);
			}
			master.execute();
			// 保存最终结算结果
			Queue<Collection<Three>> resultMap = master.getResultQueue();

			// 不需要等待所有Worker都执行完成，即可开始计算最终结果
			while (resultMap.size() > 0 || !master.isComplete()) {
				Collection<Three> col = resultMap.poll();
				if(col != null){
					for(Three three : col){
						List li = (List)three.getS();
						System.err.println(li.size());
					}
				}
			}
		}
		return null;
	}

	public static List<Three> groupBy(List<? extends Map<String, IGroupAble>> lists) {
		if (lists.size() > 100000) {

		}
		return null;
	}

	/**
	 * 均分一个大的集合为num个小的list如果不够分为num个返回尽量多个
	 * 
	 * @param source
	 *            源集合
	 * @param num
	 *            要分割成几个list
	 * @return
	 */
	private static <T> List<List<T>> shareByFixedNum(final List<T> source, final int num) {
		int sourceSize = source.size();
		int number = sourceSize / num;
		int remaider = sourceSize % num;
		int offset = 0;// 偏移量
		List<List<T>> rltList = new ArrayList<List<T>>(num);
		for (int i = 0; i < num; i++) {
			if (remaider > 0) {
				rltList.add(source.subList(i * number + offset, (i + 1) * number + offset + 1));
				remaider--;
				offset++;
			} else {
				rltList.add(source.subList(i * number + offset, (i + 1) * number + offset));
			}
		}
		return rltList;
	}

	/**
	 * 平分一个 source为若干个 list,其中每个list中的数据量大小为 listSize 个
	 * 
	 * @param source
	 *            源集合
	 * @param listSize
	 *            分割的集合容量
	 * @return
	 */
	@SuppressWarnings("unused")
	private static <T> List<List<T>> shareByFixedSize(final List<T> source, final int listSize) {
		List<List<T>> rltList = null;
		int sourceSize = source.size();
		if (sourceSize <= listSize) {
			rltList = new ArrayList<List<T>>(1);
			rltList.add(source);
			return rltList;
		}

		int remaider = sourceSize % listSize; // 余数
		int number = sourceSize / listSize; // 商数
		rltList = new ArrayList<List<T>>(remaider + 1);
		for (int i = 0; i < number; i++) {
			int toIndex;
			if ((i + 1) == number) {
				if (remaider == 0) {
					toIndex = ((i + 1) * listSize);
				} else {
					toIndex = ((i + 1) * listSize);
				}
			} else {
				toIndex = (i + 1) * listSize;
			}
			rltList.add(source.subList(i * listSize, toIndex));
		}
		if (remaider > 0) {
			rltList.add(source.subList(number * listSize, (number * listSize) + remaider));
		}
		return rltList;
	}

	// public static void main(String[] args) {
	// List<Integer> list = new LinkedList<Integer>();
	// Long rlt = 0L;
	// for (int i = 1; i < 10001; i++) {
	// list.add(i);
	// }
	// List<List<Integer>> li = shareByFixedSize(list, 101);
	// for (List<Integer> list2 : li) {
	// System.err.println(list2);
	// }
	// System.err.println("------------------------");
	// li = shareByFixedNum(list, 10);
	// for (List<Integer> list2 : li) {
	// System.err.println(list2);
	// }
	// }

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		Long rlt = 0L;
		for (int i = 1; i < 1000001; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idx", i);
			map.put("group", i % 7);
			list.add(map);
		}
		MemoryGroupUtils.groupBy(list, "group");
	}
}
