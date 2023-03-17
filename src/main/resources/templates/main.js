const url = 'http://localhost:8081/api/admin'
const tableUsers = document.getElementById('tBodyAllUsersId')
let result = ''
const newForm = document.getElementById('newModal')
let newEmail = document.getElementById('new_email')
let new_password = document.getElementById('new_password')
let new_age = document.getElementById('new_age')
let opcion = ''

//const myModalNewUser = new bootstrap.Modal(newForm)
// const link = document.getElementById('profile-tab');
// link.addEventListener('click', () => {
//     myModalNewUser.show()
// })

 $(function () {
     // await getAuthUser();
     getAllUsers();
     $('btnEdit').css('border','solid 3px green')
     //  await newUser();
//    removeUser();
     //   updateUser();

 });
const showUsers = (users) => {
    console.log(users)
    users.forEach(user => {
        result += `
    <tr class="fs-5">
        <td>${user.id}</td>
        <td>${user.email}</td>
        <td>${user.age}</td>
        <td>${user.roles.map(role => role.authority.substring(5)).join(', ')}</td>
        <td><a class="btnEdit btn btn-primary text-white " data-bs-toggle="modal"> Edit</a></td>
        <td><a class="btn btn-danger text-white btnDelete" data-bs-toggle="modal"> Delete </a></td>
    </tr>
    `
    })
    tableUsers.innerHTML = result

    // const deleteButtons = document.querySelectorAll('.btnDelete');
    // deleteButtons.forEach(button => {
    //     button.addEventListener('click', () => {
    //         console.log("удалить") // Код удаления записи
    //     });
    // });
}

//отображение таблицы
 function getAllUsers() {
    fetch(url).then(response => response.json())
        .then(data => showUsers(data))
        .catch(error => console.log(error))
}
//кнопки
const on = (element, event, selector, handler) => {
    console.log(handler)
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e)
        }
    })
}
