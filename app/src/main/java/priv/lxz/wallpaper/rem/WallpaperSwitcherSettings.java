package priv.lxz.wallpaper.rem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.limxing.expandableview.view.ExpandableView;

import de.greenrobot.event.EventBus;
import share.DataShare;

public class WallpaperSwitcherSettings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    @ViewInject(R.id.seekbar_number_brightness)
    public AppCompatSeekBar seekbar_number_brightness;
    @ViewInject(R.id.textview_num_brightness)
    public TextView textview_num_brightness;
    @ViewInject(R.id.seekbar_number_left_offset)
    public AppCompatSeekBar seekbar_number_left_offset;
    @ViewInject(R.id.textview_num_left_offset)
    public TextView textview_num_left_offset;
    @ViewInject(R.id.seekbar_number_upper_offset)
    public AppCompatSeekBar seekbar_number_upper_offset;
    @ViewInject(R.id.textview_num_upper_offset)
    public TextView textview_num_upper_offset;
    @ViewInject(R.id.checkText_barheight)
    public AppCompatCheckedTextView checkText_barheight;
    @ViewInject(R.id.checkText_adaptive_width)
    public AppCompatCheckedTextView checkText_adaptive_width;
    @ViewInject(R.id.checkText_adaptive_height)
    public AppCompatCheckedTextView checkText_adaptive_height;

    @ViewInject(R.id.seekbar_number_scale)
    public SeekBar seekbar_number_scale;
    @ViewInject(R.id.textview_num_scale)
    public TextView textview_num_scale;
    private DataShare dataShare;

    @ViewInject(R.id.expand_number_scale)
    public ExpandableView expand_number_scale;
    private AppMessageEvent event;
    private GifShowOption option;

    public AppCompatCheckedTextView checkText_custom_ratio;

    @ViewInject(R.id.checkText_check_single)
    public AppCompatCheckedTextView checkText_check_single;
    @ViewInject(R.id.checkText_check_long)
    public AppCompatCheckedTextView checkText_check_long;
    @ViewInject(R.id.checkText_check_double)
    public AppCompatCheckedTextView checkText_check_double;


    @ViewInject(R.id.checkText_play_sound)
    public AppCompatCheckedTextView checkText_play_sound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        ViewUtils.inject(this);

        initShareAndEvent();

        setTitle("壁纸参数设置");
        initView();
        initValue();

    }

    private void initShareAndEvent() {
        dataShare = DataShare.getInstance(this);
        option = dataShare.getData(GifShowOption.class);
        if (option == null) option = GifShowOption.getDefault();
        event = new AppMessageEvent(0, option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initValue() {
        seekbar_number_brightness.setProgress(option.brightness);
        seekbar_number_left_offset.setProgress(option.leftOffset);
        seekbar_number_upper_offset.setProgress(option.upperOffset);
        checkText_barheight.setChecked(option.isCalculationBarheight);
        checkText_adaptive_height.setChecked(option.isAdaptiveHeight);
        checkText_adaptive_width.setChecked(option.isAdaptiveWidth);
        checkText_custom_ratio.setChecked(option.isAdaptiveCustomRatio);
        checkText_play_sound.setChecked(option.isPlaySound);

        switch (option.trigger_mode) {
            case GifShowOption.TRIGGER_MODE_CHECK_SINGLE:
                checkText_check_single.setChecked(true);
                break;
            case GifShowOption.TRIGGER_MODE_CHECK_LONG:
                checkText_check_long.setChecked(true);
                break;
            case GifShowOption.TRIGGER_MODE_CHECK_DOUBLE:
                checkText_check_double.setChecked(true);
                break;
        }
    }

    private void initView() {




        checkText_custom_ratio = (AppCompatCheckedTextView) getLayoutInflater().inflate(R.layout.check_text_view, null);
        expand_number_scale.addTitleView(checkText_custom_ratio, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        seekbar_number_brightness.setOnSeekBarChangeListener(this);
        seekbar_number_left_offset.setOnSeekBarChangeListener(this);
        seekbar_number_upper_offset.setOnSeekBarChangeListener(this);
        seekbar_number_scale.setOnSeekBarChangeListener(this);
        seekbar_number_scale.setOnSeekBarChangeListener(this);
        checkText_barheight.setOnClickListener(this);
        checkText_adaptive_height.setOnClickListener(this);
        checkText_adaptive_width.setOnClickListener(this);
        checkText_custom_ratio.setOnClickListener(this);
        checkText_play_sound.setOnClickListener(this);

        checkText_check_single.setOnClickListener(tigger_onClick);
        checkText_check_long.setOnClickListener(tigger_onClick);
        checkText_check_double.setOnClickListener(tigger_onClick);

        seekbar_number_scale.setProgress((int)(option.scale*10));
        if (!option.isAdaptiveCustomRatio) {
            expand_number_scale.expand(false);
        }

        expand_number_scale.setOnExpandOrCollapseListen(new ExpandableView.OnExpandOrCollapseListen() {
            @Override
            public void expandBegin() {
                seekbar_number_scale.setVisibility(View.INVISIBLE);
            }

            @Override
            public void expandEnd() {
                seekbar_number_scale.setVisibility(View.VISIBLE);
            }

            @Override
            public void CollapseBegin() {
                seekbar_number_scale.setVisibility(View.INVISIBLE);
            }

            @Override
            public void CollapseEnd() {
                seekbar_number_scale.setVisibility(View.INVISIBLE);

            }
        });
    }

    public View.OnClickListener tigger_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.checkText_check_single:
                    unCheckTigger();
                    checkText_check_single.setChecked(!checkText_check_single.isChecked());
                    option.trigger_mode = GifShowOption.TRIGGER_MODE_CHECK_SINGLE;
                    break;
                case R.id.checkText_check_long:
                    unCheckTigger();
                    checkText_check_long.setChecked(!checkText_check_long.isChecked());
                    option.trigger_mode = GifShowOption.TRIGGER_MODE_CHECK_LONG;

                    break;
                case R.id.checkText_check_double:
                    unCheckTigger();
                    checkText_check_double.setChecked(!checkText_check_double.isChecked());
                    option.trigger_mode = GifShowOption.TRIGGER_MODE_CHECK_DOUBLE;
                    break;
            }
            EventBus.getDefault().post(event);
            dataShare.save(option);
        }
    };


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.checkText_barheight:
                shrink();
                checkText_barheight.setChecked(!checkText_barheight.isChecked());
                option.isCalculationBarheight = checkText_barheight.isChecked();
                break;
            case R.id.checkText_adaptive_height:
                shrink();
                unCheck_scale();
                checkText_adaptive_height.setChecked(!checkText_adaptive_height.isChecked());
                option.isAdaptiveHeight = checkText_adaptive_height.isChecked();
                break;
            case R.id.checkText_adaptive_width:
                shrink();
                unCheck_scale();
                checkText_adaptive_width.setChecked(!checkText_adaptive_width.isChecked());
                option.isAdaptiveWidth = checkText_adaptive_width.isChecked();
                break;
            case R.id.checkText_custom_ratio:
                unCheck_scale();
                if (!option.isAdaptiveCustomRatio) {
                    expand_number_scale.expandOrCollapse();
                    checkText_custom_ratio.setChecked(!checkText_custom_ratio.isChecked());
                    option.isAdaptiveCustomRatio = checkText_custom_ratio.isChecked();
                }
                break;
            case R.id.checkText_play_sound:
                checkText_play_sound.setChecked(!checkText_play_sound.isChecked());
                option.isPlaySound=checkText_play_sound.isChecked();
                break;
        }
        EventBus.getDefault().post(event);
        dataShare.save(option);

    }

    /**
     * 收缩自定义的选项
     */
    private void shrink() {
        if (option.isAdaptiveCustomRatio) {
            expand_number_scale.expandOrCollapse();
        }
    }

    public void unCheckTigger() {
        checkText_check_single.setChecked(false);
        checkText_check_long.setChecked(false);
        checkText_check_double.setChecked(false);
    }

    public void unCheck_scale() {
        checkText_adaptive_height.setChecked(false);
        checkText_adaptive_width.setChecked(false);
        checkText_custom_ratio.setChecked(false);
        option.isAdaptiveCustomRatio = false;
        option.isAdaptiveHeight = false;
        option.isAdaptiveWidth = false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()) {
            case R.id.seekbar_number_brightness:
                textview_num_brightness.setText(String.valueOf(i));
                option.brightness = i;
                break;
            case R.id.seekbar_number_left_offset:
                textview_num_left_offset.setText(String.valueOf(i));
                option.leftOffset = i;
                break;
            case R.id.seekbar_number_upper_offset:
                textview_num_upper_offset.setText(String.valueOf(i));
                option.upperOffset = i;
                break;
            case R.id.seekbar_number_scale:
                textview_num_scale.setText(new java.text.DecimalFormat("#.0").format(i * 0.1f));
                option.scale = i * 0.1f;
                break;
        }

        EventBus.getDefault().post(event);

        dataShare.save(option);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}