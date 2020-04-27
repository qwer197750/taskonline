function getCaptcha() {
    $.ajax({
        url: "/rest/captcha",
        type: "GET",
        data: {},
        dataType:"text",
        success: function (data) {
            // language=JQuery-CSS
            data = jQuery.parseJSON(data);
            $('#captchaId').val(data.captchaId);
            $("#captchaImg").attr("src",data.captchaImage);
        }, error: function () {
            xtalert.alertError("获取图片失败");
        }
    });
};
function setLoginUrl(url){
    $('#logonForm').attr("action", url);
};

$('#captchaImg').ready(function () {
    getCaptcha();
});
$('#captchaImg').click(function () {
    getCaptcha();
});