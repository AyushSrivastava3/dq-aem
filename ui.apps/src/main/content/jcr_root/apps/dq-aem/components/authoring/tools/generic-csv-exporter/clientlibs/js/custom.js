
function createCSV() {
    var form = $('form')[0]; // You need to use standard javascript object here
    var formData = new FormData(form);
    $(".results").html("");
    $.ajax({
        url: '/bin/export-content-to-csv',
        data: formData,
        method: 'POST',
        contentType: false, // NEEDED
        processData: false, // NEEDED

        success: function(data) {
            console.log("Success");
            $(".results").html(data.status);
        },
        error: function(data) {
            console.log("error", data);
            $(".results").html(data.responseText);
        }
    });
}




