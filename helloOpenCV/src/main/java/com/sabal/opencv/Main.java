package com.sabal.opencv;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sabal.helloopencv.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class Main extends Activity implements CvCameraViewListener2 {
	private CameraBridgeViewBase mOpenCvCameraView;
	private Mat mIntermediateMat;
	private int  power = 50, pin = 87,sto=45, minU=45;
	Point p, p1, p2, p3, p4;
	double Dif = 0, k = 0.35, c1, c2;
	public SeekBar Kof, nip, pow,pop;
	public TextView PowerA, DifA, pinnetta, reverer,popo;
	public int prevU, prevD, revelog = 1,maxPower = 70;
	public Robot2WD Controller;
	boolean BTconnected = false;
	public Switch Rever;
	boolean click = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.helloopencvlayout);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
		mOpenCvCameraView.setMaxFrameSize(800, 480);
		ToScreen(Integer.toString(mOpenCvCameraView.getHeight()));

		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);

		Kof = (SeekBar) findViewById(R.id.KofBar);
		Kof.setOnSeekBarChangeListener(KofBarel);

		nip = (SeekBar) findViewById(R.id.PinBar);
		nip.setOnSeekBarChangeListener(sosok);

		pow = (SeekBar) findViewById(R.id.PowerBar);
		pow.setOnSeekBarChangeListener(konopla);

		pop = (SeekBar) findViewById(R.id.popoBar);
		pop.setOnSeekBarChangeListener(popopo);		

		Rever = (Switch) findViewById(R.id.Reverse);
		DifA = (TextView) findViewById(R.id.Dif);
		PowerA = (TextView) findViewById(R.id.Power);
		pinnetta = (TextView) findViewById(R.id.Pin);
		reverer = (TextView) findViewById(R.id.RevVi);
		popo = (TextView) findViewById(R.id.popo);
		Rever.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					revelog = -1;
				} else {
					revelog = 1;
				}
			}
		});

		int typeId = getIntent().getExtras().getInt("Type");
		switch(typeId){
			case 0:
				Controller = new EV3();break;
			case 1:
				Controller = new Arduino();break;
			case 2:
				Controller = new NXT();
		}

		String Device = getIntent().getExtras().getString("Device Name", "null");
		try {
			BTconnected = Controller.Connect(Device);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (BTconnected) {
			mOpenCvCameraView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (click) {
						click = false;
						try {
							Controller.MotorsPowerOff();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						click = true;
						try {
							Controller.MotorsPowerOn();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		} else {
			ToScreen("\nNo Bluetooth connection with " + Device + "\n");
			Main.this.finish();
		}

		prevU = mOpenCvCameraView.getHeight() / 2;
		prevD = prevU;
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				mOpenCvCameraView.enableView();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat rgba = inputFrame.rgba();
		Mat grey = inputFrame.gray();
		Size sizeRgba = rgba.size();
		Mat rgbaInnerWindow;
		Mat greyInnerWindow;
		int rows = (int) sizeRgba.height;
		int cols = (int) sizeRgba.width;
		int width2 = cols / 5;
		int width = cols * 1 / 3;
		greyInnerWindow = grey.submat(0, rows, width2, width2 + width);
		rgbaInnerWindow = rgba.submat(0, rows, width2, width2 + width);

		Imgproc.cvtColor(rgbaInnerWindow, greyInnerWindow, Imgproc.COLOR_RGBA2GRAY);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(400);

		Imgproc.GaussianBlur(greyInnerWindow, greyInnerWindow, new Size(9, 9), 10);
		Imgproc.threshold(greyInnerWindow, greyInnerWindow, pin, pin + 60, Imgproc.THRESH_BINARY_INV);
		Mat greyCopy = new Mat();
		greyInnerWindow.copyTo(greyCopy);
		Imgproc.cvtColor(greyInnerWindow, rgbaInnerWindow, Imgproc.COLOR_GRAY2RGBA);
		Imgproc.findContours(greyCopy, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_NONE);
		MatOfPoint2f points = new MatOfPoint2f();
		for (int i = 0; i < contours.size(); i++) {
			contours.get(i).convertTo(points, CvType.CV_32FC2);
		}

		Point[] pcontours = points.toArray();
		Point[] forpoint = { new Point(), new Point(), new Point(), new Point() };
		for (int i = 0, c = 0; i < pcontours.length && c < 4; i++) {
			if (pcontours[i].x == 5.0 || pcontours[i].x == width - 5.0) {
				Imgproc.line(rgbaInnerWindow, pcontours[i], pcontours[i], new Scalar(0, 255, 0, 255), 15);
				forpoint[c] = pcontours[i];
				c++;
			}
		}
		if (forpoint[0].x == forpoint[2].x) {
			p = forpoint[1];
			forpoint[1] = forpoint[2];
			forpoint[2] = p;
		} else {
			p = forpoint[1];
			forpoint[1] = forpoint[3];
			forpoint[3] = p;
		}
		for (int i = 0; i < forpoint.length - 1; i++)
			for (int j = 0; j < forpoint.length - i - 1; j++)
				if (forpoint[j].x < forpoint[j + 1].x) {
					p = forpoint[j];
					forpoint[j] = forpoint[j + 1];
					forpoint[j + 1] = p;
				}
		p1 = forpoint[0];
		p2 = forpoint[1];
		p3 = forpoint[2];
		p4 = forpoint[3];

		c1 = ((p1.y + p2.y) / 2);
		c2 = ((p3.y + p4.y) / 2);
		
		if (c1 == 0 && prevU > mOpenCvCameraView.getHeight() / 2)
			c1 = mOpenCvCameraView.getHeight();
		if (c2 == 0 && prevD > mOpenCvCameraView.getHeight() / 2)
			c2 = mOpenCvCameraView.getHeight();

		prevD = (int) c2;
		prevU = (int) c1;
		
		Imgproc.arrowedLine(rgbaInnerWindow, new Point(p1.x, c1), new Point(p3.x, c2), new Scalar(0, 0, 255, 255), 13,
                4, 0, 0.2);
		Imgproc.line(rgbaInnerWindow, new Point(p1.x, c1), new Point(p1.x, c1), new Scalar(255, 0, 0, 255), 20);
		Imgproc.line(rgbaInnerWindow, new Point(p3.x, c2), new Point(p3.x, c2), new Scalar(255, 0, 0, 255), 20);

        	Dif = k * (c1 - mOpenCvCameraView.getHeight() / 2);
        	int PowerA = power + (int)Dif;
        	int PowerB = power - (int)Dif;

        	if(PowerA > maxPower)
            		PowerA = maxPower;
        	else if(PowerA < -maxPower)
              		PowerA = -maxPower;
        	if(PowerB > maxPower)
            		PowerB = maxPower;
        	else if(PowerB < -maxPower)
                	PowerB = -maxPower;

        	try {
            		Controller.MotorsPowerSet((byte) PowerA, (byte) PowerB);
        	} catch (IOException e) {
        		   e.printStackTrace();
        	}
		
		Imgproc.line(rgbaInnerWindow, new Point(0, rows), new Point(0, 0), new Scalar(200, 200, 200, 255), 1);
		Imgproc.line(rgbaInnerWindow, new Point(width - 1, rows), new Point(width - 1, 0), new Scalar(200, 200, 200, 255), 1);
		rgbaInnerWindow.release();
		return rgba;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();

	}

	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
		if (BTconnected) {
			try {
				Controller.MotorsPowerOff();
				Controller.Disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);

	}

	public void onCameraViewStarted(int width, int height) {
		mIntermediateMat = new Mat();
	}

	public void onCameraViewStopped() {
		if (mIntermediateMat != null)
			mIntermediateMat.release();
		mIntermediateMat = null;
	}

	public void ToScreen(String stroka) {
		Toast.makeText(getApplicationContext(), stroka, Toast.LENGTH_SHORT).show();
	}

	private SeekBar.OnSeekBarChangeListener KofBarel = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			k = (Kof.getProgress() + 1) * 0.05;
			k = new BigDecimal(k).setScale(2, RoundingMode.DOWN).doubleValue();
			DifA.setText(Double.toString(k));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			k = (Kof.getProgress() + 1) * 0.05;
			k = new BigDecimal(k).setScale(2, RoundingMode.DOWN).doubleValue();
			DifA.setText(Double.toString(k));
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			k = (Kof.getProgress() + 1) * 0.05;
			k = new BigDecimal(k).setScale(2, RoundingMode.DOWN).doubleValue();
			DifA.setText(Double.toString(k));
		}
	};

	private SeekBar.OnSeekBarChangeListener sosok = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			pin = nip.getProgress();
			pinnetta.setText(Integer.toString(pin));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			pin = nip.getProgress();
			pinnetta.setText(Integer.toString(pin));
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			pin = nip.getProgress();
			pinnetta.setText(Integer.toString(pin));
		}
	};

	private SeekBar.OnSeekBarChangeListener konopla = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			power = pow.getProgress();
			PowerA.setText(Integer.toString(power));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			power = pow.getProgress();
			PowerA.setText(Integer.toString(power));
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			power = pow.getProgress();
			PowerA.setText(Integer.toString(power));
		}
	};

	private SeekBar.OnSeekBarChangeListener popopo = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			maxPower = pop.getProgress();
			popo.setText(Integer.toString(maxPower));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			maxPower = pop.getProgress();
			popo.setText(Integer.toString(maxPower));
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			maxPower = pop.getProgress();
			popo.setText(Integer.toString(maxPower));
		}
	};
	
}
