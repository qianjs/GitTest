package com.bigkoo.pickerview.view;

import java.util.ArrayList;
import android.view.View;

import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;

public class WheelOptions<T> {
	private View view;
	private WheelView wv_option1;
	private WheelView wv_option2;
	private WheelView wv_option3;

	private ArrayList<T> mOptions1Items;
	private ArrayList<ArrayList<T>> mOptions2Items;
	private ArrayList<ArrayList<ArrayList<T>>> mOptions3Items;

    private boolean linkage = false;
    private OnItemSelectedListener wheelListener_option1;
    private OnItemSelectedListener wheelListener_option2;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelOptions(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void setPicker(ArrayList<T> optionsItems) {
		setPicker(optionsItems, null, null, false);
	}

	public void setPicker(ArrayList<T> options1Items,
			ArrayList<ArrayList<T>> options2Items, boolean linkage) {
		setPicker(options1Items, options2Items, null, linkage);
	}

	public void setPicker(ArrayList<T> options1Items,
			ArrayList<ArrayList<T>> options2Items,
			ArrayList<ArrayList<ArrayList<T>>> options3Items,
			boolean linkage) {
        this.linkage = linkage;
		this.mOptions1Items = options1Items;
		this.mOptions2Items = options2Items;
		this.mOptions3Items = options3Items;
		int len = ArrayWheelAdapter.DEFAULT_LENGTH;
		if (this.mOptions3Items == null)
			len = 8;
		if (this.mOptions2Items == null)
			len = 12;
		// 閫夐」1
		wv_option1 = (WheelView) view.findViewById(R.id.options1);
		wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 璁剧疆鏄剧ず鏁版嵁
		wv_option1.setCurrentItem(0);// 鍒濆鍖栨椂鏄剧ず鐨勬暟鎹�		// 閫夐」2
		wv_option2 = (WheelView) view.findViewById(R.id.options2);
		if (mOptions2Items != null)
			wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items.get(0)));// 璁剧疆鏄剧ず鏁版嵁
		wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 鍒濆鍖栨椂鏄剧ず鐨勬暟鎹�		// 閫夐」3
		wv_option3 = (WheelView) view.findViewById(R.id.options3);
		if (mOptions3Items != null)
			wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items.get(0).get(0)));// 璁剧疆鏄剧ず鏁版嵁
		wv_option3.setCurrentItem(wv_option3.getCurrentItem());// 鍒濆鍖栨椂鏄剧ず鐨勬暟鎹�
		int textSize = 25;

		wv_option1.setTextSize(textSize);
		wv_option2.setTextSize(textSize);
		wv_option3.setTextSize(textSize);

		if (this.mOptions2Items == null)
			wv_option2.setVisibility(View.GONE);
		if (this.mOptions3Items == null)
			wv_option3.setVisibility(View.GONE);

		// 鑱斿姩鐩戝惉鍣� 
		wheelListener_option1 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				int opt2Select = 0;
				if (mOptions2Items != null) {
                    opt2Select = wv_option2.getCurrentItem();//涓婁竴涓猳pt2鐨勯�涓綅缃�					//鏂皁pt2鐨勪綅缃紝鍒ゆ柇濡傛灉鏃т綅缃病鏈夎秴杩囨暟鎹寖鍥达紝鍒欐部鐢ㄦ棫浣嶇疆锛屽惁鍒欓�涓渶鍚庝竴椤�                    
                    opt2Select = opt2Select >= mOptions2Items.get(index).size() - 1 ? mOptions2Items.get(index).size() - 1 : opt2Select;

					wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
							.get(index)));
					wv_option2.setCurrentItem(opt2Select);
				}
				if (mOptions3Items != null) {
                    wheelListener_option2.onItemSelected(opt2Select);
				}
			}
		};
        wheelListener_option2 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				if (mOptions3Items != null) {
                    int opt1Select = wv_option1.getCurrentItem();
                    opt1Select = opt1Select >= mOptions3Items.size() - 1 ? mOptions3Items.size() - 1 : opt1Select;
                    index = index >= mOptions2Items.get(opt1Select).size() - 1 ?  mOptions2Items.get(opt1Select).size() - 1 : index;
					int opt3 = wv_option3.getCurrentItem();//涓婁竴涓猳pt3鐨勯�涓綅缃�                    //鏂皁pt3鐨勪綅缃紝鍒ゆ柇濡傛灉鏃т綅缃病鏈夎秴杩囨暟鎹寖鍥达紝鍒欐部鐢ㄦ棫浣嶇疆锛屽惁鍒欓�涓渶鍚庝竴椤�                    
					opt3 = opt3 >= mOptions3Items.get(opt1Select).get(index).size() - 1 ? mOptions3Items.get(opt1Select).get(index).size() - 1 : opt3;

					wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items
							.get(wv_option1.getCurrentItem()).get(
                                    index)));
					wv_option3.setCurrentItem(opt3);

				}
			}
		};

//		// 娣诲姞鑱斿姩鐩戝惉
		if (options2Items != null && linkage)
			wv_option1.setOnItemSelectedListener(wheelListener_option1);
		if (options3Items != null && linkage)
			wv_option2.setOnItemSelectedListener(wheelListener_option2);
	}

	/**
	 * 璁剧疆閫夐」鐨勫崟浣�	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		if (label1 != null)
			wv_option1.setLabel(label1);
		if (label2 != null)
			wv_option2.setLabel(label2);
		if (label3 != null)
			wv_option3.setLabel(label3);
	}

	/**
	 * 璁剧疆鏄惁寰幆婊氬姩
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
		wv_option2.setCyclic(cyclic);
		wv_option3.setCyclic(cyclic);
	}

	/**
	 * 鍒嗗埆璁剧疆绗竴浜屼笁绾ф槸鍚﹀惊鐜粴鍔�	 *
	 * @param cyclic1,cyclic2,cyclic3
	 */
	public void setCyclic(boolean cyclic1,boolean cyclic2,boolean cyclic3) {
        wv_option1.setCyclic(cyclic1);
        wv_option2.setCyclic(cyclic2);
        wv_option3.setCyclic(cyclic3);
	}
    /**
     * 璁剧疆绗簩绾ф槸鍚﹀惊鐜粴鍔�     *
     * @param cyclic
     */
    public void setOption2Cyclic(boolean cyclic) {
        wv_option2.setCyclic(cyclic);
    }
/**
     * 璁剧疆绗笁绾ф槸鍚﹀惊鐜粴鍔�     *
     * @param cyclic
     */
    public void setOption3Cyclic(boolean cyclic) {
        wv_option3.setCyclic(cyclic);
    }

	/**
	 * 杩斿洖褰撳墠閫変腑鐨勭粨鏋滃搴旂殑浣嶇疆鏁扮粍 鍥犱负鏀寔涓夌骇鑱斿姩鏁堟灉锛屽垎涓変釜绾у埆绱㈠紩锛�锛�锛�
	 * 
	 * @return
	 */
	public int[] getCurrentItems() {
		int[] currentItems = new int[3];
		currentItems[0] = wv_option1.getCurrentItem();
		currentItems[1] = wv_option2.getCurrentItem();
		currentItems[2] = wv_option3.getCurrentItem();
		return currentItems;
	}

	public void setCurrentItems(int option1, int option2, int option3) {
        if(linkage){
            itemSelected(option1, option2, option3);
        }
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
        wv_option3.setCurrentItem(option3);
	}

	private void itemSelected(int opt1Select, int opt2Select, int opt3Select) {
		if (mOptions2Items != null) {
			wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items
					.get(opt1Select)));
			wv_option2.setCurrentItem(opt2Select);
		}
		if (mOptions3Items != null) {
			wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items
					.get(opt1Select).get(
							opt2Select)));
			wv_option3.setCurrentItem(opt3Select);
		}
	}


}
