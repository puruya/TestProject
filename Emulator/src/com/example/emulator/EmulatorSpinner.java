package com.example.emulator;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class EmulatorSpinner extends Activity {
	private final int COMMAND=1;
	private final int NUMBER=2;
	private final int CHARACTER=3;
	public String show=null;
//	private int check_flag=0;
//	private int check_flag2=0;
	public String TAG="SPINNER";
	Spinner mSpinner1,mSpinner2,mSpinner3,mSpinner_select;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_spinner0);
	    Spinner mSpinner_select= (Spinner)findViewById(R.id.spinner0);
	    ArrayAdapter<CharSequence> select = ArrayAdapter.createFromResource(this, R.array.select, android.R.layout.simple_dropdown_item_1line);
	    mSpinner_select.setAdapter(select);
		 mSpinner_select.setOnItemSelectedListener(new OnItemSelectedListener() {
				 //position하고 id가 있어. 
			 		   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { 

						    choose_spinner(position);	
					}	 
			
				      public void onNothingSelected(AdapterView<?> parent) {
				  
				         	  Toast.makeText(EmulatorSpinner.this, "unselected", Toast.LENGTH_SHORT).show();		       
				      }
				     
				   });
		 
		 
		 //  finish();
	}
	
	void choose_spinner(int position){
		ArrayAdapter<CharSequence> adapter, adapter2, adapter3 ;
		switch(position){
		case COMMAND:
			 setContentView(R.layout.activity_spinner1);
			 Spinner mSpinner1 = (Spinner) findViewById(R.id.spinner1);
		    adapter = ArrayAdapter.createFromResource(this, R.array.command, android.R.layout.simple_spinner_item);
		     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			  mSpinner1.setAdapter(adapter);
			  
			  mSpinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			 //position하고 id가 있어. 
				   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					  // show = (String) adapter.getItem(position);
					  // 이렇게 할때 안된다? final로 설정해야되 왜그런걸까?
			
					   show = (String) parent.getSelectedItem();
					   Toast.makeText(EmulatorSpinner.this, show, Toast.LENGTH_SHORT).show();
					   get_key_number(COMMAND, position);
					  
				   }
			 
			      public void onNothingSelected(AdapterView<?> parent) {
			         	  Toast.makeText(EmulatorSpinner.this, "Spinner1: unselected", Toast.LENGTH_SHORT).show();
			       }
			   });
			break;
		case NUMBER:
			//spinner 2
		
			 setContentView(R.layout.activity_spinner2);
			Spinner mSpinner2 = (Spinner) findViewById(R.id.spinner2);
		    adapter2 = ArrayAdapter.createFromResource(this, R.array.number, android.R.layout.simple_spinner_item);
	       adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
			/// 왜 어뎁터 돌려쓰면안되? setAdapter(adapter)하면 nullpointer exception 발생
			mSpinner2.setAdapter(adapter2);
			  mSpinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
				   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					   show = (String) parent.getSelectedItem(); 
					   Toast.makeText(EmulatorSpinner.this, show, Toast.LENGTH_SHORT).show();
                        get_key_number(NUMBER, position);   
						
				   }
			      public void onNothingSelected(AdapterView<?> parent) {
			         	  Toast.makeText(EmulatorSpinner.this, "Spinner2: unselected", Toast.LENGTH_SHORT).show();
			       }
			   });
			break;
		case CHARACTER:
			 //spinner 3
	 setContentView(R.layout.activity_spinner3);
			  Spinner mSpinner3 = (Spinner) findViewById(R.id.spinner3);
			  adapter3 = ArrayAdapter.createFromResource(this, R.array.character, android.R.layout.simple_spinner_item);
			  adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	    
			  mSpinner3.setAdapter(adapter3);
			  mSpinner3.setOnItemSelectedListener(new OnItemSelectedListener() {
			 
				   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {	 
					   show = (String) parent.getSelectedItem();
					   Toast.makeText(EmulatorSpinner.this, show, Toast.LENGTH_SHORT).show();
					   get_key_number(CHARACTER, position);
					  
				   }
			 
			      public void onNothingSelected(AdapterView<?> parent) {
			         	  Toast.makeText(EmulatorSpinner.this, "Spinner3: unselected", Toast.LENGTH_SHORT).show();
			       }
			   });
		}
		
	}
	
	void get_key_number(int list, int position){
		int set_number = 0;
		
		if(list==NUMBER){
			set_number=6+position;
		}
		else if(list==CHARACTER){
			set_number=28+position;
		}
		else if(list==COMMAND){
			if(position>3){
				//26,27,28이니까 /
				set_number=22+position;
			}
			else{
				set_number = 3+position;
			}
		}
		else{
			//errorcheck
			Log.e("SPINNEr","spinner error");
		}
		/*cannot refer a non-fianl variable set_number inside an inner class defined in a different method)
		new Thread(new Runnable(){
			public void run(){
				new Instrumentation().sendKeyDownUpSync(set_number);
			}
		}).start();*/
		press_key(set_number);
		
	}
	///아놔...파이널처리 모르겟음...
	void press_key(final int value){
		new Thread(new Runnable(){
			public void run(){
				new Instrumentation().sendKeyDownUpSync(value);
			}
		}).start();
	}
}


