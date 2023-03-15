const url = 'http://localhost:8081/api/admin'
const container = document.querySelector('tbody')
let result = ''
const newForm = document.getElementById('new-form')
let newEmail = document.getElementById('new_email')
let new_password = document.getElementById('new_password')
let new_age = document.getElementById('new_age')
let opcion = ''

const myModalNewUser = new bootstrap.Modal(document.getElementById('new_user'))
const link = document.getElementById('profile-tab');
link.addEventListener('click', () => {
    myModalNewUser.show()
})

const showData = (users) => {
    console.log(users)
    users.forEach(user => {
        result += `
    <tr>
    <td>${user.id}</td>
    <td>${user.email}</td>
    <td>${user.age}</td>
    <td>${user.roles}</td>
</tr>
    `
    })
//     for (let i = 0; i < data.length; i++) {
//         result += `<tr><td>${data[i].id}</td>
//                     <td>${data[i].email}</td>
//                     <td>${data[i].age}</td>
//                     <td>${data[i].roles}</td>
//                     </tr>`
//     }

    container.innerHTML = result
}

//отображение таблицы
fetch(url).then(response => response.json())
    .then(data => showData(data))
    .catch(error => console.log(error))