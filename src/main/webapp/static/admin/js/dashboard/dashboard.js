var app = new Vue({
  el: '#wrapper',
  data: {
    /**
     * System 
     */
    loading: true,

    /**
     * Service
     */
    user: null,
    viewCount: null,
    postCount: null,
    replyCount: null,
    resCount: null,
  },
  method: {
    hello: function() {
      
    }
  },
  created: function() {
    const vue = this;
    
    $.ajax({
      
    });
  }
})