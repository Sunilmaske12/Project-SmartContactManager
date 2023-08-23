/*function toggleSidebar {
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "2px");
	}else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20px");
	}
}*/


/*=================================delete contact================================*/
function deleteContact(cid){
	console.log(cid);
	swal({
  title: "Are you sure?",
  text: "Once deleted, not be able to recover!",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
	  console.log("if");
    window.location="/Normal/deleteContact/"+cid;
  } else {
    swal("Your contact  is safe!");
     console.log("else");
  }
});
}

	
	
		function test(){
			alert("hi");
		}
	