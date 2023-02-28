window.sessionStorage;

console.log("start loadBankProperties " + performance.now());

loadBankProperties().then(function() {
    console.log("done loadBankProperties " + performance.now());

    $("#banklogo").attr("src", "/images/" + sessionStorage.getItem("bankCode") + "-logo.png");
    $("#banklogo").attr("alt", sessionStorage.getItem("bankName"));
    $("#bankname").text(sessionStorage.getItem("bankName"));
    $("#bankslogan").text(sessionStorage.getItem("bankSlogan"));

    setWindowTitle("Welcome");
});

function playMusic() {
    toggleLlamaboss(true);
    const audio = document.querySelector("audio");
    audio.volume = 0.2;
    if (audio.paused){
        audio.play();
    } else{
        audio.pause();
        toggleLlamaboss(false);
    }
}

function toggleLlamaboss(boolean){
    if (boolean == true){
        endOfString = "-gif.gif";
    } else {
        endOfString = "-logo.png";
    }
    $("#banklogo").attr("src", "/images/" + sessionStorage.getItem("bankCode") + endOfString);
}