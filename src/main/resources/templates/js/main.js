const url = 'http://localhost:8081/api/admin'
const container = document.querySelector('tbody')
let result = ''
const newForm = document.getElementById("new-form")
let newEmail = document.getElementById("new_email")
let new_password = document.getElementById("new_password")
let new_age = document.getElementById("new_age")


const showData = (arrays) => {
    arrays.forEach(arr => {
        result += `
    <tr>
    <td>${arr.id}</td>
    <td>${arr.newEmail}</td>
</tr>
    `
    })
    container.innerHTML = result
}

//отображение таблицы
fetch(url).then(response => response.json())
    .then(data => showData(data))
    .catch(error => console.log(error))