(function makeDiv() {
    //var divsize = ((Math.random() * 100) + 50).toFixed();
    //var color = '#' + Math.round(0xffffff * Math.random()).toString(16);
    //$newdiv = $('<span class="lighting"/>');

    //var posx = (Math.random() * ($(document).width() - divsize)).toFixed();
    //var posy = (Math.random() * ($(document).height() - divsize)).toFixed();

    //$newdiv.css({
    //    'left': posx + 'px',
    //    'top': posy + 'px',
    //    'display': 'none'
    //}).appendTo('body').fadeIn(100).delay(300).fadeOut(250, function() {
    //    $(this).remove();
    //    makeDiv();
    //});

})();

var body = $('body');

function goToNextInput(e) {
    var key = e.which,
        t = $(e.target),
        sib = t.next('input');

    if (key != 9 && (key < 48 || key > 57)) {
        e.preventDefault();
        return false;
    }

    if (key === 9) {
        return true;
    }

    if (!sib || !sib.length) {
        sib = body.find('input').eq(0);
    }
    sib.select().focus();
}

function onKeyDown(e) {
    var key = e.which;
    return true;

    if (key === 9 || (key >= 48 && key <= 57)) {
        return true;
    }

    e.preventDefault();
    return false;
}

function onFocus(e) {
    $(e.target).select();
}

body.on('keyup', 'input.otp-input', goToNextInput);
body.on('keydown', 'input.otp-input', onKeyDown);
body.on('click', 'input.otp-input', onFocus);
// ----------
var lastQuery = "";
var searchValue = "";
var timer = null;
if (typeof $ == 'undefined') {
    var $ = jQuery;
}

// Them 0 vao dau so
function pad(str, max) {
    str = str.toString();
    return str.length < max ? pad("0" + str, max) : str;
}

function suggestionFunc() {
    if (searchValue.length < 2) {
        $('#search-suggestion').fadeOut(100);
        return false;
    } else {

        if (lastQuery == "") {
            lastQuery = searchValue;
        } else if (lastQuery == searchValue) {
            $('#search-suggestion').fadeIn(100);
            return;
        }

        var link = "/search/suggest";
        $.ajax({
            type: "POST",
            url: link,
            cache: false,
            data: {
                'keyword': searchValue
            },
            success: function(data) {
                $('#search-suggestion').html(data);
            },
            error: function(request, status, err) {
                console.log(err);
            },
            complete: function() {}
        });
    }
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
$(document).ready(function() {

    $('.myluck-sound').each(function() {
        $(this).get(0).load();
    })
    $('body').on('click tap', function() {
        //var bgSound = document.getElementById("sound-bg");
        //bgSound.volume = 0.2;
        //bgSound.play();
    });
    if ($('#game-container').length) {
        $('#game-container').css('min-height', ($(window).height() ) + 'px');
    }
    if ($('#game-container2').length) {
        $('#game-container2').css('min-height', ($(window).height() ) + 'px');
        $('#game-container').css('min-height', ($(window).height() ) + 'px');
    }
    $(document).on('click', '.ajax-btn', function() {
        var modalId = $(this).attr('data-target');
        var modalBody = $(modalId).find('#modal-body');
        modalBody.html('<center><img src="/images/ajax-loader.gif" /></center>');
        if (($(modalId).data('bs.modal') || {})._isShown) {
            modalBody.load($(this).attr('ahref'));
            //dynamiclly set the header for the modal
            //document.getElementById('modalHeader').innerHTML = '<h4>' + $(this).attr('title') + '</h4>';
        } else {
            //if modal isn't open; open it and load content
            $(modalId).modal('show')
                .find('#modal-body')
                .load($(this).attr('ahref'));
            //dynamiclly set the header for the modal
            //document.getElementById('modalHeader').innerHTML = '<h4>' + $(this).attr('title') + '</h4>';
        }
    });
    $('.modal-content').on('submit', '.ajax-modal-form', function(e) {

        var form = $(this);
        e.preventDefault();
        $.ajax({
            url: form.attr('action'),
            type: form.attr('method'),
            data: new FormData(form[0]),
            mimeType: 'multipart/form-data',
            contentType: false,
            cache: false,
            processData: false,
            //dataType: 'json',
            success: function(data) {
                $('#modal-body').html(data);
            }
        });
        return false;
    });

    var ajaxLoading = false;
    $('.ajax-form').on('submit', function(e) {
        var form = $(this);
        var resultContainer = $(form.attr('result-container'));
        // resultContainer.html('<div class="loader-wrapper"><img src="/images/ajax-loader.gif" /></div>');
        $('#ajax-loader').show();

        e.preventDefault();

        setTimeout(function() {
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
                    //dataType: 'json',
                    success: function(data) {

                        resultContainer.html(data);
                    },
                    complete: function() {
                        ajaxLoading = false;
                        $('#ajax-loader').hide();
                    }
                });
            }

        }, 500);

        return false;
    });
    $('.ajax-form').on('change', '.form-control', function() {
        $(this).parent().parent().parent().submit();
    });
    $(document).mouseup(function(e) {
        var container = $("#search-form");

        // if the target of the click isn't the container nor a descendant of the container
        if (!container.is(e.target) && container.has(e.target).length === 0) {
            $('#search-suggestion').hide();
        }
    });

    $(".btn-menu-mobile").click(function() {
        $(".main-menu-mobile").addClass('open');
        $(".overlay").addClass('open');
    });

    $(".btn-close-menu").click(function() {
        $(".main-menu-mobile").removeClass('open');
        $(".overlay").removeClass('open');
    });
    $(".overlay").click(function() {
        $(".main-menu-mobile").removeClass('open');
        $(".overlay").removeClass('open');
    });

    var prevScrollpos = window.pageYOffset;
    window.onscroll = function() {
        var currentScrollPos = window.pageYOffset;
        if (prevScrollpos > currentScrollPos) {
            document.getElementById("header").style.top = "0";
            //document.getElementById("bottommenu").style.bottom = "0";
        } else if (currentScrollPos > 70) {
            document.getElementById("header").style.top = "-52px";
            //document.getElementById("bottommenu").style.bottom = "-50px";
        }
        prevScrollpos = currentScrollPos;
    }

    $('.ml-dropup').on('click', function() {
        $('#footer-ad-menu').show();
        return false;
    });
    $('.ad-close').on('click', function() {
        $('#footer-ad-menu').hide();
    });
    $(".modal").on('show.bs.modal', function() {
        var sound = document.getElementById("sound-confirm");
        sound.play();
    });
    $(".modal").on('hidden.bs.modal', function() {
        var sound = document.getElementById("sound-confirm");
        sound.pause();
        sound.currentTime = 0;
    });
});