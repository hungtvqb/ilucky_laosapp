$(document).ready(function() {

    var ajaxLoading = false;
    $('#play-confirm-form').on('submit', function(e) {
        var form = $(this);
        var resultContainer = $('#result-modal');
        var loadingSound = $('#sound-loading').get(0);
        var displaySound = $('#sound-display-prize').get(0);

        $('#ajax-loading').show();
        loadingSound.play();

        $('.modal').modal('hide');
        e.preventDefault();


        if (!ajaxLoading) {
            ajaxLoading = true;
            $.ajax({
                url: form.attr('action'),
                type: 'post',
                data: new FormData(form[0]),
                mimeType: 'multipart/form-data',
                contentType: false,
                cache: false,
                processData: false,
                dataType: 'json',
                success: function(resp) {
                    loadingSound.pause();
                    loadingSound.currentTime = 0;

                    if (resp.errorCode == '0') {
                        $('.modal').modal('hide');
                        console.log(resp.data['win_prizes']);
                        var html = '';
                        jQuery.each(resp.data['win_prizes'], function(index, prize) {
                            html += '<div class="prize"><img class="prize-icon" src="' + prize.PRIZE_ICON + '" /><br/>' + prize.PRIZE_VALUE + prize.PRIZE_UNIT +
                                ((prize.EXPIRED_TEXT) ? ' (' + prize.EXPIRED_TEXT + ')' : '') + '</div>';

                        });
                        $('#prize-list').html(html);

                        var html2 = '';
                        jQuery.each(resp.data['lucky_codes'], function(index, luckycode) {
                            html2 += '<div class="luckycode">' + luckycode + '</div>';
                        });
                        $('#luckycode-list').html(html2);
                        resultContainer.show();
                        displaySound.play();
                    } else {
                        toastr.warning(resp.message, '', {
                            timeOut: 10000,
                            showDuration: 1000,
                            extendedTimeOut: 2000
                        });
                    }

                },
                complete: function() {
                    ajaxLoading = false;
                    loadingSound.pause();
                    loadingSound.currentTime = 0;
                    $('#ajax-loading').hide();
                }
            });
        }



        return false;
    });

    $('#play-confirm-modal').on('hidden.bs.modal', function() {
        $('#game-packs .clicked img').addClass('mbounce')
        $('#game-packs .clicked').removeClass('clicked');
    });
});