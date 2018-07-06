const functions = require('firebase-functions');


///jobAdvert/publishedAdverts/{jobId}/applyInfo/ 

//bu adresi dinliyorum buraya birşey eklendiğinde write metodu tetikleniyor data içersinde tutuluyor..


exports.countlikechange = functions.database.ref('/jobAdvert/publishedAdverts/{jobId}/applyInfo/').onWrite(
    (data) => {
 		const collectionRef = data.after.ref; ///jobAdvert/publishedAdverts/{jobId}/applyInfo/{userId} 
 		const applyRef= collectionRef.parent;//applyInfo
 		const countRef = collectionRef.parent.child('jobInfo').child('countApply'); //jobId->jobInfo->countApply
    
		console.log('collectionRef',collectionRef); //-->JobId de  
		console.log('myRef',myRef); //-->
		console.log('countRef',collectionRef);

	//applyinfo  içerisibde tüm childleri(numChildren()) sayıyorum ve countApply'a set ediyorum 
return applyRef.child('applyInfo').once('value')
     .then((messagesData) => countRef.set(messagesData.numChildren()));

});


	

