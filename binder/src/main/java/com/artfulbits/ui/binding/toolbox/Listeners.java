package com.artfulbits.ui.binding.toolbox;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.artfulbits.ui.binding.Listener;
import com.artfulbits.ui.binding.Notifications;
import com.artfulbits.ui.binding.Selector;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/** Implementation of common listeners. */
@SuppressWarnings("unused")
public final class Listeners {
  /* [ CONSTANTS ] ================================================================================================= */

  /** Generic EMPTY listener. */
  public final static Listener NONE = new Listener() {
    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      // do nothing...
      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications selector) {
      // do nothing
    }

    @Override
    public void detach(@NonNull final Notifications selector) {
      // do nothing
    }
  };
  /** Extra key used for delivering listener ID to broadcast processing class. */
  private final static String EXT_KEY = "key";
  /** Action name used for timer based data exchange scheduling. */
  private final static String ACTION_TIME = "action_time_comes";

  /* [ STATIC MEMBERS ] ============================================================================================ */

  /** Broadcasts updates synchronization guard. */
  private static final Object sSync = new Object();
  /** Global map for alarm manager pending intents processing. */
  private static final Map<Integer, WeakReference<Listener>> sBroadcast = new HashMap<>();
  /** Instance of broadcast receiver that will process our timer based data exchange requests. */
  private static AlarmReceiver sReceiver;

	/* [ CONSTRUCTORS ] ============================================================================================== */

  /** hidden constructor. */
  private Listeners() {
    throw new AssertionError();
  }

	/* [ STATIC METHODS ] ============================================================================================ */

  /**
   * Join multiple listeners into one expression.
   *
   * @param listeners the array of listeners
   * @return the joined listeners instance.
   */
  @NonNull
  public static Listener anyOf(@NonNull final Listener... listeners) {
    return new Listener() {
      @Override
      public Listener binding(@NonNull final Selector<?, ?> instance) {
        for (Listener listener : listeners) {
          listener.binding(instance);
        }

        return this;
      }

      @Override
      public void willNotify(@NonNull final Notifications selector) {
        for (Listener listener : listeners) {
          listener.willNotify(selector);
        }
      }

      @Override
      public void detach(@NonNull final Notifications selector) {
        for (Listener listener : listeners) {
          listener.detach(selector);
        }
      }
    };
  }

  /**
   * Get 'empty listener' instance. this is simply a stub that can be used in expressions but it doing absolutely
   * nothing. You can add Logs into stub, for making binding process more transparent.
   */
  @NonNull
  public static Listener none() {
    return NONE;
  }

  /** Raise notification on any change from Observable side. */
  @NonNull
  public static Listener onObservable() {
    return new ObserverListener();
  }

  /** Listen to notifications from Observable instance and raise notification only if filter matched. */
  @NonNull
  public static Listener onObservable(final Object filter) {
    return new ObserverListener(filter);
  }

  /** Detect string changes and raise data exchange on that. */
  @NonNull
  public static Listener onTextChanged() {
    return new TextWatcherListener();
  }

  /** Detect focus loss and raise data exchange on that. */
  @NonNull
  public static Listener onFocusLost() {
    return new FocusLostListener();
  }

  /** Timer based raiser of data exchange. */
  @NonNull
  public static Listener onTimer(@IntRange(from = 0) final long delay,
                                 @IntRange(from = 0) final long interval) {
    return new TimerListener(delay, interval);
  }

  /** Timer based raiser of data exchange. Based on more resource effective Alarm Manager. */
  @NonNull
  public static Listener onAlarm(@NonNull final Context context,
                                 @IntRange(from = 0) final long delay,
                                 @IntRange(from = 0) final long interval) {
    return new AlarmListener(context, delay, interval);
  }

  /* [ IMPLEMENTATION ] ============================================================================================ */

  /** Register listener and if needed create Receiver for events processing. */
  private static void register(@NonNull final Context context, @NonNull final Listener listener) {
    // register our instance as a broadcast listener
    sBroadcast.put(listener.hashCode(), new WeakReference<>(listener));

    // create instance of Broadcast receiver
    if (null == sReceiver) {
      synchronized (sSync) {
        if (null == sReceiver) {
          context.registerReceiver(sReceiver = new AlarmReceiver(), new IntentFilter(ACTION_TIME));
        }
      }
    }
  }

  /** Unregister the listener and release OS resources. */
  private static void unregister(@NonNull final Context context, @NonNull final Listener listener) {
    sBroadcast.remove(listener.hashCode());

    if (sBroadcast.isEmpty()) {
      synchronized (sSync) {
        if (sBroadcast.isEmpty()) {
          context.unregisterReceiver(sReceiver);
          sReceiver = null;
        }
      }
    }
  }

	/* [ NESTED DECLARATIONS ] ======================================================================================= */

  /** Listen to texts changes of the TextView control. */
  private static class TextWatcherListener implements Listener, TextWatcher {
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();

    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
      for (Notifications selector : mKnown) {
        selector.onChanged();
      }
    }

    @Override
    public void afterTextChanged(final Editable s) {

    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      final Object view = instance.getRuntimeInstance();

      if (view instanceof TextView) {
        final TextView tv = (TextView) view;

        tv.addTextChangedListener(this);
      }

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);
    }
  }

  /** Listen to focus loss of the View. */
  private static class FocusLostListener implements Listener, View.OnFocusChangeListener {
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();

    @Override
    public void onFocusChange(final View v, final boolean hasFocus) {
      // we listen only focus loss
      if (hasFocus) return;

      for (Notifications selector : mKnown) {
        selector.onChanged();
      }
    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      final Object view = instance.getRuntimeInstance();

      if (view instanceof View) {
        final View tv = (View) view;

        tv.setOnFocusChangeListener(this);
      }

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);
    }
  }

  /** Listen to Observable instance. */
  private static class ObserverListener implements Listener, Observer {
    /** filter that allows any kind of data be treated as a notification to listener. */
    public static final Object ANYTHING = new Object();
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();
    /** Filter instance. */
    private final Object mFilter;

    /** No filter, allowed all notifications. */
    public ObserverListener() {
      this(ANYTHING);
    }

    /** Define custom filter. */
    public ObserverListener(final Object filter) {
      mFilter = filter;
    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      final Object i = instance.getRuntimeInstance();

      if (i instanceof Observable) {
        final Observable ob = (Observable) i;

        ob.addObserver(this);
      }

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);
    }

    @Override
    public void update(final Observable observable, final Object data) {
      if (ANYTHING.equals(mFilter) || mFilter.equals(data)) {
        for (Notifications selector : mKnown) {
          selector.onChanged();
        }
      }
    }
  }

  /** Raise event by timer. */
  private static class TimerListener extends TimerTask implements Listener {
    /** Create instance of timer for countdown. Timer is created with daemon flag. */
    private final Timer mCountDown = new Timer(true);
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();
    /** Delay in milliseconds. */
    private final long mDelay;
    /** Period of repeat. */
    private final long mPeriod;

    /** new instance with delay and period settings defining. */
    public TimerListener(@IntRange(from = 0) final long delay, @IntRange(from = 0) final long period) {
      mDelay = delay;
      mPeriod = period;
    }

    @Override
    public void run() {
      for (Notifications selector : mKnown) {
        selector.onChanged();
      }
    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      mCountDown.schedule(this, mDelay, mPeriod);

      return this;
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);
    }
  }

  /** Broadcast receiver for processing the AlarmListener requests. */
  private static class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
      // ignore wrong calls
      if (null == context || null == intent) return;

      // get listener that request the processing
      final int key = intent.getIntExtra(EXT_KEY, -1);
      final WeakReference<Listener> wrl = sBroadcast.get(key);
      final Listener l;

      // nothing to process
      if (null == wrl || null == (l = wrl.get())) return;

      // if listener is a known to us type, than raise the changed state
      if (l instanceof AlarmListener) {
        ((AlarmListener) l).raise();
      }
    }
  }

  /**
   * Timer based listener, but instead of separated thread used AlarmManager which is more battery effective in compare
   * to background thread.
   */
  private static class AlarmListener implements Listener {
    /** Application context. */
    private final Context mContext;
    /** Set of notifiers. */
    private final Set<Notifications> mKnown = new HashSet<>();
    /** Delay in milliseconds. */
    private final long mDelay;
    /** Period of repeat. */
    private final long mPeriod;
    /** pending operation. */
    private PendingIntent mOperation;

    /** Create instance that will start raising 'changed' state in specified delay and with specified interval. */
    public AlarmListener(@NonNull final Context context,
                         @IntRange(from = 0) final long delay,
                         @IntRange(from = 0) final long interval) {
      mContext = context;
      mDelay = delay;
      mPeriod = interval;
    }

    @Override
    public Listener binding(@NonNull final Selector<?, ?> instance) {
      // register our instance as a broadcast listener
      register(mContext, this);

      // create intent that will be delivered by Alarm Manager
      final Intent intent = new Intent(mContext, AlarmReceiver.class)
          .setAction(ACTION_TIME)
          .putExtra(EXT_KEY, hashCode());

      final PendingIntent operation = PendingIntent.getBroadcast(mContext, hashCode(),
          intent, PendingIntent.FLAG_UPDATE_CURRENT);

      // set the timer
      final AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
      manager.setRepeating(AlarmManager.ELAPSED_REALTIME,
          SystemClock.elapsedRealtime() + mDelay, mPeriod,
          operation);

      return this;
    }

    /** Notify all listeners that its time for data exchange. */
    public void raise() {
      for (Notifications selector : mKnown) {
        selector.onChanged();
      }
    }

    @Override
    public void willNotify(@NonNull final Notifications listener) {
      mKnown.add(listener);
    }

    @Override
    public void detach(@NonNull final Notifications listener) {
      mKnown.remove(listener);

      if (mKnown.isEmpty() && null != mOperation) {
        final AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(mOperation);

        unregister(mContext, this);
      }
    }
  }
}
