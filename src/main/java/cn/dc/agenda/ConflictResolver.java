package cn.dc.agenda;

import java.util.Comparator;


public class ConflictResolver implements Comparator<Activation> {

	public int compare(Activation o1, Activation o2) {
		if(o1.isModifyObject()==o2.isModifyObject()){
			return o2.getSalience()-o1.getSalience();
		}else if(o1.isModifyObject()==true){
			return 1;
		}else {
			return -1;
		}
		
	}

	

}
