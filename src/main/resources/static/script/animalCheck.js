$(function() {
    $("#animal-select").on("change", function(){
        var animalSelect = $(this).val();
        $.get("/userAnimals/" + animalSelect, function (animal) {
            $("#animalId").val(animal.id);
            var imageId;
            if (animal.images.length > 0) {
                imageId = animal.images[animal.images.length - 1].id;
                $("#imageId").attr("src", "/story/image?id=" + imageId);
            } else {
                $("#imageId").attr("src", "//placehold.it/100");
            }

            $("#id").val(animal.id);
            $("#name").val(animal.name);
            $("#type").val(animal.breed.type.id);
            var typeId = animal.breed.type.id;
            console.log(type);
            $.get("/breeds/" + typeId, function (breeds) {
                var breedId = $("#breed");
                breedId.empty();
                breeds.forEach(function(breed){
                    var breedOption = $("<option value='" + breed.id + "'>" + breed.name + "</option>");
                    breedId.append(breedOption);
                });
            });
            $("#breed").val(animal.breed.id);
            $("#age").val(animal.age);
            $("#weight").val(animal.weight);
            $("#gender").val(animal.gender);
            $("#tumblr").val(animal.tumblr);
            $("#story").val(animal.story);
            $("#careInfo").val(animal.careInfo);
        });
    });
});
