
$(function() {
  $("#type").on("change", function(){
      var type = $(this).val();
      $.get("/breeds/" + type, function (breeds) {
          var breedId = $("#breed");
          breedId.empty();
          breeds.forEach(function(breed){
              var breedOption = $("<option value='" + breed.id + "'>" + breed.name + "</option>");
              breedId.append(breedOption);
          });
      });
  });
});