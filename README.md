# TNetProgress
###  Hello,这是一个文件上传、下载的进度监听库
  使用简单.无论okhttp还是retrofit方式,只需要在oKhttpClient的构建者对象中添加拦截器即可.<br>
  下载拦截器:DownloadInterceptor.<br>
  上传拦截器:UploadInterceptor.<br>
###  提示:
  Demo细致演示了使用方式.<br> 
  为了不添加冗余代码,进度数据变化体现在Button文本上,更直观是进度条.<br>
###  预览图:
  <img width="350"  src="https://github.com/HoldMyOwn/TNetProgress/blob/master/preview/all.gif" /><br>
###  用法:
<pre>
     //添加下载拦截器(this参数是实现下载进度接口的对象) 
     mDownClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownloadInterceptor(this))
                .build();
                
     //添加上传拦截器(this参数是实现上传回调接口的对象)            
     mUploadClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new UploadInterceptor(this))
                .build();
                
 </pre>
###   事件回调:
<pre>
    //info包含总长度、当前长度、进度百分比、请求url等信息
    @Override
    public void onDownLoadProgress(ProgressInfo info) {
            if (info.getPercentFloat() == 1) {
                mDownloadBN.setText("下载完成   总尺寸" + String.format("Size : %s", FileUtil.getFileSize(info.getContentLength())));
                mDownloadBN.setEnabled(true);
                return;
            }
            mDownloadBN.setText("下载:" + info.getPercentString());
    }
    @Override
    public void onUpLoadProgress(ProgressInfo info) {
            if (info.getPercentFloat() == 1) {
                mUploadBN.setText("上传完成   总尺寸" + String.format("Size : %s", FileUtil.getFileSize(info.getContentLength())));
                mUploadBN.setEnabled(true);
                return;
            }
            mUploadBN.setText("上传:" + info.getPercentString());
    }
    
</pre>

###   具体细节用法,下载查看Demo
###   模板依赖:&nbsp;&nbsp;项目里面的tnetprogress模板(可更加灵活扩展)
###   gradle依赖:&nbsp;&nbsp;&nbsp;compile&nbsp;'com.jkt:tnetprogress:1.0.2'

