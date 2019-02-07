package com.example.bluetoothlibrary;

import com.example.bluetoothlibrary.entity.Peripheral;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SettingUtil {
	public static String mDeviceAddress;
	public static List<Peripheral> preipherals = new ArrayList<Peripheral>();

	public static String tempVersion = "";//
	public static int BTBattery = 0;//
	public static int testType = 0;//
	public static boolean isGusetmode = false;//
	public static int userModeSelect = 0;
	public static int isResponse = 0;//
	public static int WBPMODE = 0;//
	public static int ischangeF = 0;//
	public static boolean isfirstLoad = true;
	// public static int isfirstLoad2 = 0;//
	// public static int isfirstLoad3 = 0;
	// public static int isfirstLoad4 = 0;
	public static boolean isTest = false;//

	public static int Sbp;//
	public static int Dbp;//
	public static int Hr;//


	public final static String KPA = "kPa";
	public final static String MMKG = "mmHg";

	public static String tempDate = "";

	public static boolean isNotHaveBtn = false;
	public static boolean isNowTestBp;
	public static boolean isNowTestBpFinish = false;

	public static int isfirstLoad5 = 0;
	public static int isfirstLoad6 = 0;
	public static int isfirstLoad7 = 0;
	public static int isfirstLoad8 = 0;
	public static int isfirstLoad9 = 0;
	public static int isfirstLoad10 = 0;

	public static boolean isSyncFinish = false;

	public static String guestBpTime = "";

	public static boolean isopenBle = false;
	public static String bleState = "0";
	public static boolean isFirstopenBle = true;
	public static boolean isFirstPhoneopenBle = true;
	public static boolean resendData = false;
	public static boolean isHaveData = false;
	public static boolean isdismiss = false;

	public static List<String> listBp = new ArrayList<String>();


	public static boolean isEnglishedOrIsNumericOrIsChinese(String text) {
		Pattern pattern = Pattern.compile("[a-zA-Z_0-9\u4e00-\u9fa5]*");

		Matcher isNum = pattern.matcher(text);

		return isNum.matches();
	}


	public static int KpaToMmhg(float kpa) {
		float tempUnit = 7.5006168f;
		float tempmmhg = tempUnit * kpa;
		BigDecimal b = new BigDecimal(tempmmhg);
		int mmhg = b.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		return mmhg;
	}


	public static float MmhgToKpa(int mmhg) {
		float tempUnit = 0.1333224f;
		float tempkpa = tempUnit * mmhg;
		BigDecimal b = new BigDecimal(tempkpa);
		float kpa = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		return kpa;
	}


	public static int getBPState(int Sbp, int Dbp) {
		int bpState = 0;
		if (Sbp >= 180) {
			bpState = 6;
		} else if (Sbp > 160) {
			bpState = 5;
		} else if (Sbp > 140) {
			bpState = 4;
		} else if (Sbp > 130) {
			bpState = 3;
		} else if (Sbp > 120) {
			if (Dbp >= 110) {
				bpState = 6;
			} else if (Dbp > 100) {
				bpState = 5;
			} else if (Dbp > 90) {
				bpState = 4;
			} else if (Dbp > 85) {
				bpState = 3;
			} else if (Dbp > 80) {
				bpState = 2;
			} else if (Dbp > 50) {
				bpState = 1;
			} else {
				bpState = 0;
			}
		} else if (Sbp > 90) {
			bpState = 1;
		} else {
			bpState = 0;
		}
		return bpState;
	}


	public static void setResumeData() {
		isfirstLoad5 = 0;
		isfirstLoad8 = 0;
		isfirstLoad7 = 0;
		isfirstLoad6 = 0;
		isfirstLoad9 = 0;
		isfirstLoad10 = 0;
	}
}
