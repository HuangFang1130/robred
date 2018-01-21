package com.jiahehongye.robred.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NoticeTrigger_MGR implements NoticeObserver {
    private static final int TIME = 150;
	private volatile static NoticeTrigger_MGR instance;

	private ArrayList<NoticeTriggerListener> observers = new ArrayList<NoticeTriggerListener>();

	private ScheduledThreadPoolExecutor executor = null;

	private NoticeTrigger_MGR() {
		super();
		init();
	}

	public static NoticeTrigger_MGR Instance() {
		if (instance == null) {
			synchronized (NoticeTrigger_MGR.class) {
				if (instance == null) {
					instance = new NoticeTrigger_MGR();
				}
			}
		}
		return instance;
	}

	private void init() {
		requestMessageNotifyTask();
	}

	/**
	 * 请求轮询消息通知信息
	 */
	private void requestMessageNotifyTask() {
		executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable task) {
				return new Thread(task, "MessageNotify");
			}
		});
		// 每隔10S请求查询一次,首次进入延时10S后执行
		executor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
                NoticeTrigger noticeTrigger = new NoticeTrigger();
                noticeTrigger.setTriggerID(NoticeTriggerID.ANIMATION_FRAME_Notify);
                notifyTopicbserver(noticeTrigger);
			}
		}, 0, TIME, TimeUnit.MILLISECONDS);
	}

	@Override
	public void registerTopicObserver(NoticeTriggerListener observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public void removeTopicObserver(NoticeTriggerListener observer) {
		int index = observers.indexOf(observer);
		if (index >= 0) {
			observers.remove(index);
		}
	}

    private int index = 0;
	@Override
	public void notifyTopicbserver(final NoticeTrigger trigger) {
		new Handler(Looper.getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 123) {
					for (int i = 0; i < observers.size(); i++) {
						NoticeTriggerListener observer = (NoticeTriggerListener) observers
								.get(i);
						observer.onTopicTrigger(trigger);
					}
				}
				super.handleMessage(msg);
			}

		}.sendEmptyMessage(123);
	}


    public void sendEmptyTrigger(NoticeTriggerID triggerID) {
        NoticeTrigger noticeTrigger = new NoticeTrigger();
        noticeTrigger.setTriggerID(triggerID);
        notifyTopicbserver(noticeTrigger);
    }
}
