package com.examples.joshuayingwhat.latte.ec.main.personal.profile;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.examples.joshuayingwhat.latte.delegates.LatteDelegate;
import com.examples.joshuayingwhat.latte.ec.R;
import com.examples.joshuayingwhat.latte.ec.main.personal.list.ListBean;
import com.examples.joshuayingwhat.latte.net.RestClient;
import com.examples.joshuayingwhat.latte.net.callback.ISuccess;
import com.examples.joshuayingwhat.latte.net.rx.RxRestClient;
import com.examples.joshuayingwhat.latte.utils.callback.CallBackManager;
import com.examples.joshuayingwhat.latte.utils.callback.CallBackType;
import com.examples.joshuayingwhat.latte.utils.callback.IGlobalCallBack;
import com.joshuayingwhat.latte_ui.ui.data.DataDialogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * setting中item的点击事件
 */
public class UseProfileClickListener extends SimpleClickListener {

    private final LatteDelegate delegate;

    private String[] mGenders = new String[]{"男", "女", "保密"};

    UseProfileClickListener(LatteDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final ListBean bean = (ListBean) baseQuickAdapter.getData().get(position);
        final int id = bean.getmId();
        switch (id) {
            case 1:
                CallBackManager.getInstance().addCallBack(CallBackType.ON_CROP, new IGlobalCallBack<Uri>() {
                    @Override
                    public void executeCallBack(Uri args) {
                        final ImageView avatar = (ImageView) view.findViewById(R.id.img_arrow_avatar);
                        Glide.with(delegate)
                                .load(args)
                                .into(avatar);
                        //上传图像信息
                        RestClient.builder().url(UploadConfig.UPLOAD_IMG)
                                .loader(delegate.getContext())
                                .file(args.getPath())
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        final String path = JSON.parseObject(response).getJSONObject("result")
                                                .getString("path");

                                        //通知服务器更新信息
                                        RxRestClient.builder()
                                                .url("user_profile.php")
                                                .params("avatar", path)
                                                .loader(delegate.getContext())
                                                .build()
                                                .post()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<String>() {
                                                    @Override
                                                    public void accept(String response) throws Exception {
                                                        //获取更新后的用户信息，然后更新本地数据库
                                                        //没有本地数据的APP，每次打开APP都请求API，获取信息
                                                    }
                                                });
                                    }
                                }).build().upLoad();
                    }
                });
                //打开相机
                delegate.startCameraWithCheck();
                break;
            case 2:
                final LatteDelegate nameDelegate = bean.getmDelegate();
                delegate.getSupportDelegate().start(nameDelegate);
                break;
            case 3:
                getGenderDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final TextView textView = view.findViewById(R.id.tv_arrow_value);
                        textView.setText(mGenders[which]);
                        dialog.cancel();
                    }
                });
                break;
            case 4:
                final DataDialogUtil dialogUtil = new DataDialogUtil();
                dialogUtil.setDateListener(new DataDialogUtil.IDateListener() {
                    @Override
                    public void onDateChange(String date) {
                        final TextView textView = (TextView) view.findViewById(R.id.tv_arrow_value);
                        textView.setText(date);
                    }
                });
                dialogUtil.showDialog(delegate.getContext());
                break;
            default:
                break;
        }
    }

    private void getGenderDialog(DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(delegate.getContext());
        builder.setSingleChoiceItems(mGenders, 0, listener);
        builder.show();
    }

    @Override
    public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
