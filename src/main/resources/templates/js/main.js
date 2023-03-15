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

const showUsers = (users) => {
    console.log(users)
    users.forEach(user => {
        result += `
    <tr class="fs-5">
        <td>${user.id}</td>
        <td>${user.email}</td>
        <td>${user.age}</td>
        <td>${user.roles.map(role=>role.role.substring(5)).join(', ')}</td>
    </tr>
    `
    })
    container.innerHTML = result
}

//отображение таблицы
fetch(url).then(response => response.json())
    .then(data => showUsers(data))
    .catch(error => console.log(error))