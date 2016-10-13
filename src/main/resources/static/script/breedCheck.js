
$(function() {
  $("#type").on("change", function(){
      var type = $(this).val();
      $.get("/breeds/" + type, function (breeds) {
          var breedId = $("#breed");
          breedId.empty();
          var breedOption = $("<option value=''>select...</option>");
          breedId.append(breedOption);
          breeds.forEach(function(breed){
              var breedOption = $("<option value='" + breed.id + "'>" + breed.name + "</option>");
              breedId.append(breedOption);
          });
      });
  });
});