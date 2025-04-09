# shopping-app

- Screenshots
- Product list screen
  ![image](https://github.com/user-attachments/assets/74ad359a-1468-41e1-a459-1f9f868248b2)
- Product details screen
  ![image](https://github.com/user-attachments/assets/93fc4e15-09ff-4fb3-8553-adcb0a5990cb)
- Cart screen
  ![image](https://github.com/user-attachments/assets/eef05edd-f4f6-4f20-b120-f9de1862f077)
- Order Summary screen
  ![image](https://github.com/user-attachments/assets/68b739b9-4abd-46eb-b1f7-80b0c4e930dd)

- Firestore Database
  
Table deatils
1. users
   -userID(string)
   -createdDate(string)
   - userMailID(string)
   - userName(string)
   - userRole(string)(Admin/Customer)
2. productsp
   - roductID(string)
   - createdDate(string)
   - imageName(string)
   - price(number)
   - productDetails(string)
   - productName(string)
   - productQuantity(number)
   - productVisibility(Boolean)
4. order
   - orderID(string)
   - address(string)
   - cartItems(array)
   - createdDate(string)
   - orderStatus(string)
   - totalCartAmount(number)
   - userMailID
6. address
   - addressID(string)
   - createdDate(string)
   - address(string)
   - pinCode(string)
   - userMailID(string)
     
- Storage
1. Folder name
   - productListImage
