package com.dgmltn.porterduff;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

// http://android-er.blogspot.com/2012/09/porterduffcolorfilter-demonstration.html
public class MainActivity extends Activity {
	ImageView imageView;

	SeekBar alphaBar, redBar, greenBar, blueBar;
	Spinner modeSpinner;
	TextView colorInfo;

	PorterDuff.Mode[] optMode = PorterDuff.Mode.values();

	String[] optModeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageView = (ImageView) findViewById(R.id.iv);

		alphaBar = (SeekBar) findViewById(R.id.bar_a);
		redBar = (SeekBar) findViewById(R.id.bar_r);
		greenBar = (SeekBar) findViewById(R.id.bar_g);
		blueBar = (SeekBar) findViewById(R.id.bar_b);

		colorInfo = (TextView) findViewById(R.id.colorinfo);

		alphaBar.setOnSeekBarChangeListener(colorBarChangeListener);
		redBar.setOnSeekBarChangeListener(colorBarChangeListener);
		greenBar.setOnSeekBarChangeListener(colorBarChangeListener);
		blueBar.setOnSeekBarChangeListener(colorBarChangeListener);

		modeSpinner = (Spinner) findViewById(R.id.mode);
		prepareOptModeName();
		ArrayAdapter<String> modeArrayAdapter = new ArrayAdapter<String>(
				this,
				android.R.layout.simple_spinner_item,
				optModeName);
		modeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		modeSpinner.setAdapter(modeArrayAdapter);
		modeSpinner.setSelection(0);
		modeSpinner.setOnItemSelectedListener(onModeItemSelectedListener);

		setPorterDuffColorFilter(imageView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Automatically triggered by the clipboard button
	public void clipboard(View view) {
		// Gets a handle to the clipboard service.
		ClipboardManager clipboard = (ClipboardManager)
				getSystemService(Context.CLIPBOARD_SERVICE);

		// Creates a new text clip to put on the clipboard
		ClipData clip = ClipData.newPlainText("PorterDuff code", generateCode());
		
		// Set the clipboard's primary clip.
		clipboard.setPrimaryClip(clip);
	}

	private void prepareOptModeName() {
		optModeName = new String[optMode.length];

		for (int i = 0; i < optMode.length; i++) {
			optModeName[i] = optMode[i].toString();
		}
	}

	OnSeekBarChangeListener colorBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			setPorterDuffColorFilter(imageView);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}
	};

	OnItemSelectedListener onModeItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			setPorterDuffColorFilter(imageView);

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

	private void setPorterDuffColorFilter(ImageView iv) {

		int srcColor = Color.argb(
				alphaBar.getProgress(),
				redBar.getProgress(),
				greenBar.getProgress(),
				blueBar.getProgress());

		PorterDuff.Mode mode = optMode[modeSpinner.getSelectedItemPosition()];

		PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(srcColor, mode);
		iv.setColorFilter(porterDuffColorFilter);

		colorInfo.setText(generateCode());
		//		colorInfo.setText(
		//				"srcColor = #" + Integer.toHexString(srcColor) + "\n" +
		//						"mode = " + String.valueOf(mode.toString()) + " of total " + String.valueOf(optMode.length)
		//						+ " modes.");

	}

	private String generateCode() {
		PorterDuff.Mode mode = optMode[modeSpinner.getSelectedItemPosition()];

		String code = "int srcColor = Color.argb(0x" + Integer.toHexString(alphaBar.getProgress())
				+ ", 0x" + Integer.toHexString(redBar.getProgress())
				+ ", 0x" + Integer.toHexString(greenBar.getProgress())
				+ ", 0x" + Integer.toHexString(blueBar.getProgress())
				+ ");\n"
				+ "PorterDuff.Mode mode = PorterDuff.Mode." + String.valueOf(mode.toString()) + ";\n"
				+ "PorterDuffColorFilter filter = new PorterDuffColorFilter(srcColor, mode);\n"
				+ "imageView.getBackground().setColorFilter(filter);\n";

		return code;
	}
}
