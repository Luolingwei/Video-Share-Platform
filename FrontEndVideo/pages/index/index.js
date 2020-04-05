const app = getApp()

Page({
  data: {
    // 用于分页的属性
    totalPage: 1,
    page:1,
    videoList:[],
    screenWidth: 350,
    serverUrl: "",
    searchContent: ""
  },

  onLoad: function (params) {
    var me = this;
    var serverUrl = app.serverUrl;
    var screenWidth = wx.getSystemInfoSync().screenWidth;
    me.setData({
      screenWidth: screenWidth,
      serverUrl: serverUrl,
    });

    var searchContent = params.searchValue;
    var isSaveRecord = params.isSaveRecord;
    if (isSaveRecord == null || isSaveRecord == '' || isSaveRecord == undefined){
      isSaveRecord = 0;
    }

    me.setData({
      searchContent: searchContent
    })

    var page = me.data.page;
    me.getAllVideoList(page, isSaveRecord);

  },

  getAllVideoList: function (page, isSaveRecord) {
    var me = this;
    var serverUrl = app.serverUrl;
    wx.showLoading({
      title: '请等待，加载中...',
    });

    var searchContent = me.data.searchContent;

    wx.request({
      url: serverUrl + '/video/showAll?page=' + page + '&isSaveRecord=' + isSaveRecord,
      method: "POST",
      data: {
        videoDesc: searchContent
      },
      success: function (res) {
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        wx.stopPullDownRefresh();

        console.log(res.data);

        // 判断当前页page是否是第一页，如果是第一页，那么设置videoList为空 (为了下拉刷新设置)
        if (page === 1) {
          me.setData({
            videoList: []
          });
        }

        var videoList = res.data.data.rows;
        var newVideoList = me.data.videoList;

        me.setData({
          videoList: newVideoList.concat(videoList),
          page: page,
          totalPage: res.data.data.total
        });

      }
    })
  },

  onPullDownRefresh: function() {
    var me = this;
    wx.showNavigationBarLoading();
    me.getAllVideoList(1,0);
  },
  

  onReachBottom:function() {
    var me = this;
    var curPage = me.data.page;
    var totalPage = me.data.totalPage;
    // 所有视频Page已用完
    if (curPage==totalPage){
      wx.showToast({
        title: '已经没有视频啦~~',
        icon: 'none'
      })
      return;
    }
    // 视频Page还未用完
    var page = curPage + 1;
    me.getAllVideoList(page,0);


    
  },

  showVideoInfo: function(e) {

  }

})
