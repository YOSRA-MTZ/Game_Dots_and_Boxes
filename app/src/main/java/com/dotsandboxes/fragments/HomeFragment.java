package com.dotsandboxes.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dotsandboxes.R;
import com.dotsandboxes.activities.MainActivity;
import com.dotsandboxes.activities.MusicPlayerActivity;
import com.dotsandboxes.services.MusicPlayerService;
import com.dotsandboxes.utils.Constants;
import com.dotsandboxes.utils.PrefUtils;
import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends Fragment {


    public PrefUtils prefUtils;
    @BindView(R.id.btn_share)
    ImageView btnShare;

    @BindView(R.id.btn_profile)
    ImageView btnProfile;
    @BindView(R.id.tv_selected_grid)
    TextView tvSelectedGrid;
    @BindView(R.id.ll_grid_size)
    LinearLayout llGridSize;
    @BindView(R.id.ll_choose_grid_size)
    FrameLayout llChooseGridSize;
    @BindView(R.id.img_player_1_name_vs_robot)
    ImageView imgPlayer1NameVsRobot;
    @BindView(R.id.tv_player_1_name_vs_robot)
    TextView tvPlayer1NameVsRobot;
    @BindView(R.id.img_player_2_name_vs_robot)
    ImageView imgPlayer2NameVsRobot;
    @BindView(R.id.tv_player_2_name_vs_robot)
    TextView tvPlayer2NameVsRobot;
    @BindView(R.id.ll_me_vs_robot)
    LinearLayout llMeVsRobot;
    @BindView(R.id.img_player_1_name_vs_friend)
    ImageView imgPlayer1NameVsFriend;
    @BindView(R.id.tv_player_1_name_vs_friend)
    TextView tvPlayer1NameVsFriend;
    @BindView(R.id.img_player_2_name_vs_friend)
    ImageView imgPlayer2NameVsFriend;
    @BindView(R.id.tv_player_2_name_vs_friend)
    TextView tvPlayer2NameVsFriend;
    @BindView(R.id.ll_me_vs_friend)
    LinearLayout llMeVsFriend;
    @BindView(R.id.img_player_1_name_vs_online_friend)
    ImageView imgPlayer1NameVsOnlineFriend;
    @BindView(R.id.tv_player_1_name_vs_online_friend)
    TextView tvPlayer1NameVsOnlineFriend;
    @BindView(R.id.img_player_2_name_vs_online_friend)
    ImageView imgPlayer2NameVsOnlineFriend;
    @BindView(R.id.tv_player_2_name_vs_online_friend)
    TextView tvPlayer2NameVsOnlineFriend;
    @BindView(R.id.ll_me_vs_online)
    LinearLayout llMeVsOnline;
    @BindView(R.id.ll_choose_game_type)
    LinearLayout llChooseGameType;
    @BindView(R.id.btn_history)
    ImageView btnHistory;
    @BindView(R.id.btn_setting)
    ImageView btnSetting;
    @BindView(R.id.btn_how_to)
    ImageView btnHowTo;

    String selectedRowItem = "4";
    String selectedColumnItem = "4";
    Animation expandIn = null;
    boolean isAnimate;
    private Pair<String, String> rowColumnPair = new Pair<>("4", "4");
    private String nameFromPrefrence = "";
    private String imageFromPreference = "";
    private String row;
    private String column;
    private PlayerNameFragment playerNameFragment;

    public HomeFragment() {
        // Required empty public constructor
    }
    public HomeFragment(boolean isAnimate) {
        this.isAnimate = isAnimate;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        if (isAnimate)
            setAnimation();
        init();
        return root;
    }

    @Override
    public void onDestroy() {
        if (playerNameFragment!=null){
            playerNameFragment.dismiss();
        }
        super.onDestroy();
    }

    @OnClick({R.id.btn_share, R.id.btn_profile, R.id.ll_grid_size, R.id.ll_me_vs_robot, R.id.ll_me_vs_friend, R.id.ll_me_vs_online, R.id.btn_history, R.id.btn_setting, R.id.btn_how_to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_share:

                break;

            case R.id.btn_profile:
                //((MainActivity) getActivity()).hideLoader();
                ((MainActivity) getActivity()).replaceFragment(new ProfileFragment(), false);
                break;
            case R.id.ll_grid_size:
                showPopupMenu(llGridSize);
                break;
            case R.id.ll_me_vs_robot:
                 row = rowColumnPair.first;
                 column = rowColumnPair.second;
                ((MainActivity) getActivity()).setRobotGameData(row, column,Constants.ROBOT,nameFromPrefrence,imageFromPreference);
                Bundle args = new Bundle();
                args.putInt(Constants.SELECTED_ROW, Integer.parseInt(row));
                args.putInt(Constants.SELECTED_COLUMN, Integer.parseInt(column));
                args.putString(Constants.GAME_MODE, Constants.ROBOT);
                args.putString(Constants.PLAYER1_NAME, ((MainActivity) getActivity()).player1Name);
                args.putString(Constants.PLAYER2_NAME, ((MainActivity) getActivity()).player2Name);
           /* args.putString(Constants.prefrences.NAME, playerNameMe);
            args.putString(Constants.prefrences.PROFILE_IMAGE, playerMeImage);*/
                ((MainActivity) getActivity()).gameFragment = new GameFragment();
                ((MainActivity) getActivity()).gameFragment.setArguments(args);
                ((MainActivity) getActivity()).replaceFragment(((MainActivity) getActivity()).gameFragment, false);
                ((MainActivity) getActivity()).loadGameFragment();
                break;
            case R.id.ll_me_vs_friend:
                 row = rowColumnPair.first;
                 column = rowColumnPair.second;
                ((MainActivity) getActivity()).setFriendGameData(row, column,nameFromPrefrence,imageFromPreference);
                 playerNameFragment = new PlayerNameFragment();
                playerNameFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                playerNameFragment.show(getActivity().getSupportFragmentManager(), "dialog_fragment");
                playerNameFragment.setCancelable(true);
                break;
            case R.id.ll_me_vs_online:
                if (prefUtils.getBoolean(Constants.IS_GOOGLE_SIGN_IN)) {
                    String rowOnline = rowColumnPair.first;
                    String columnOnline = rowColumnPair.second;
                    ((MainActivity) getActivity()).invitePlayers(rowOnline, columnOnline);
                }else{

                    final Dialog dialogSort = new Dialog(getActivity());
                    dialogSort.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogSort.setContentView(R.layout.fragment_no_play_game_service);
                    dialogSort.setCancelable(false);
                    dialogSort.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    AppCompatTextView tvCancel = dialogSort.findViewById(R.id.tv_cancel);
                    AppCompatTextView tvOk = dialogSort.findViewById(R.id.tv_ok);
                    tvOk.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            dialogSort.dismiss();
                        }
                    });
                    tvCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogSort.dismiss();
                        }
                    });
                    if (!dialogSort.isShowing())
                        dialogSort.show();
                    Window windowView = dialogSort.getWindow();
                    windowView.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
                break;
            case R.id.btn_history:
                ((MainActivity) getActivity()).replaceFragment(new HistoryFragment(), false);
                break;
            case R.id.btn_setting:
                ((MainActivity) getActivity()).replaceFragment(new SettingsFragment(), false);
                break;
            case R.id.btn_how_to:
                ((MainActivity) getActivity()).replaceFragment(new HowToFragment(), false);
                break;
        }
    }


    /**
     * this function will start animation
     */
    private void setAnimation() {
        final PropertyAction share = PropertyAction.newPropertyAction(btnShare).scaleX(0).scaleY(0).duration(300).interpolator(new AccelerateDecelerateInterpolator()).build();
        //final PropertyAction music = PropertyAction.newPropertyAction(btnMusic).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction profile = PropertyAction.newPropertyAction(btnProfile).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction histroy = PropertyAction.newPropertyAction(btnHistory).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction setting = PropertyAction.newPropertyAction(btnSetting).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction howTo = PropertyAction.newPropertyAction(btnHowTo).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction gridSize = PropertyAction.newPropertyAction(llChooseGridSize).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        final PropertyAction gameType = PropertyAction.newPropertyAction(llChooseGameType).scaleX(0).scaleY(0).duration(500).interpolator(new AccelerateDecelerateInterpolator()).build();
        Player.init().animate(share).
                //then().animate(music).
                then().animate(profile).
                then().animate(gridSize).
                then().animate(gameType).
                then().animate(histroy).
                then().animate(setting).
                then().animate(howTo).
                play();

    }

    private void init() {
        ((MainActivity) getActivity()).commonToolbar.setVisibility(View.GONE);
     //   ((MainActivity) getActivity()).frameContainer.setBackground(getActivity().getResources().getDrawable(R.drawable.home_screen_background));
        prefUtils = new PrefUtils(getActivity());

        nameFromPrefrence = prefUtils.getString(Constants.prefrences.NAME);
        imageFromPreference = prefUtils.getString(Constants.prefrences.PROFILE_IMAGE);

//        Timber.e("\n \n Name = " + nameFromPrefrence + " and image url = " + imageFromPreference);

        boolean playMusic = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(getString(R.string.pref_key_music), true);



        if (nameFromPrefrence != null && !TextUtils.isEmpty(nameFromPrefrence)) {
            tvPlayer1NameVsRobot.setText(nameFromPrefrence);
            tvPlayer1NameVsFriend.setText(nameFromPrefrence);
            tvPlayer1NameVsOnlineFriend.setText(nameFromPrefrence);
        } else {
            tvPlayer1NameVsRobot.setText(getString(R.string.me));
            tvPlayer1NameVsFriend.setText(getString(R.string.me));
            tvPlayer1NameVsOnlineFriend.setText(getString(R.string.me));
        }



    }

    private void shareDotsAndBoxes() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi there, are you interested in playing Dots & boxes with me?");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    /**
     * Open popup menu to choose grid size for game
     *
     * @param layout
     */
    public void showPopupMenu(LinearLayout layout) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.choose_grid_menu, null);

        TextView four = (TextView) popupView.findViewById(R.id.four);
        TextView five = (TextView) popupView.findViewById(R.id.five);
        TextView six = (TextView) popupView.findViewById(R.id.six);
        TextView seven = (TextView) popupView.findViewById(R.id.seven);
        TextView eight = (TextView) popupView.findViewById(R.id.eight);
        TextView nine = (TextView) popupView.findViewById(R.id.nine);


        PopupWindow popupWindow = new PopupWindow(
                popupView,
                layout.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        popupWindow.showAsDropDown(layout);// your view instance in which click you want to show menu

        // Do your customised stuff

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClicked(four.getText().toString());
                popupWindow.dismiss();
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClicked(five.getText().toString());
                popupWindow.dismiss();
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClicked(six.getText().toString());
                popupWindow.dismiss();
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClicked(seven.getText().toString());
                popupWindow.dismiss();
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClicked(eight.getText().toString());
                popupWindow.dismiss();
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupClicked(nine.getText().toString());
                popupWindow.dismiss();
            }
        });


    }

    /**
     * This method is used to choose grid size, as value passed for grid size
     *
     * @param value the for grid size, how many row and column do you want
     */
    public void onPopupClicked(String value) {
        tvSelectedGrid.setText(value);
        switch (value) {
            case Constants.GRID_SIZE.FOUR_BY_FOUR:
                rowColumnPair = new Pair<String, String>("4", "4");
                break;
            case Constants.GRID_SIZE.FIVE_BY_FIVE:
                rowColumnPair = new Pair<String, String>("5", "5");
                break;
            case Constants.GRID_SIZE.SIX_BY_SIX:
                rowColumnPair = new Pair<String, String>("6", "6");
                break;
            case Constants.GRID_SIZE.SEVEN_BY_SEVEN:
                rowColumnPair = new Pair<String, String>("7", "7");
                break;
            case Constants.GRID_SIZE.EIGHT_BY_EIGHT:
                rowColumnPair = new Pair<String, String>("8", "8");
                break;
            case Constants.GRID_SIZE.NINE_BY_NINE:
                rowColumnPair = new Pair<String, String>("9", "9");
                break;
            case Constants.GRID_SIZE.TEN_BY_TEN:
                rowColumnPair = new Pair<String, String>("10", "10");
                break;
        }
    }

}
