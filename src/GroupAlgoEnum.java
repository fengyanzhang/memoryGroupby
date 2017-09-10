import java.util.List;
import java.util.Map;

public enum GroupAlgoEnum implements IGroupAlgo<Map<String, Object>> {

	AVG {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			
		}
	}, // 平均数

	ROW2COLUMN {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // 行转列

	SUM {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // 求和

	COUNT {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // 计数

	MAX {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}, // 最大值

	MIN {

		/**
		 * 
		 */
		public void callGroup(List<Map<String, Object>> list, Map<String, Object> t, Object converge) {
			// TODO Auto-generated method stub

		}
	}; // 最小值

	private GroupAlgoEnum() {

	}

}
