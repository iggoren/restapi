$(async function () {
    await getAuthUser();
    await getAllUser();
    await newUser();
    removeUser();
    updateUser();

});

async function getAuthUser() {
    fetch("http://localhost:8082/user/auth")
        .then(response => response.json())
        .then(data => {
            $('#userMail').append(data.email);
            $('#userRole').append(data.role[0].roleName.substring(5));
            let user = `$(
            <tr class="fs-5">
                <td>${data.id}</td>
                <td>${data.firstName}</td>
                <td>${data.lastName}</td>
                <td>${data.age}</td>
                <td>${data.email}</td>
                <td>${data.stringRole}</td>)`;
            $('#userTable').append(user);
        })
        .catch(error => console.log(error))
}

async function getAllUser() {
    const table = $('#usersTable');
    table.empty();
    fetch("http://localhost:8082/admin/all")
        .then(response => response.json())
        .then(dataUsers => {
            dataUsers.forEach(user => {
                let usersTable = `$(
                <tr class="fs-5">
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.stringRole}</td>)
                    <td>
                        <button class="btn btn-info text-white" type="button" data-userid="${user.id}" data-action="Edit" 
                                data-bs-toggle="modal" data-bs-target="#editModal">Edit</button>
                    </td>
                    <td>
                        <button class="btn btn-danger" type="button" data-userid="${user.id}" data-action="Delete"
                                data-bs-toggle="modal" data-bs-target="#delModal">Delete</button>
                    </td>
                </tr>)`;
                table.append(usersTable);
            })
        })
        .catch(error => console.log(error))
}

async function getUser(id) {
    let url = "http://localhost:8082/admin/" + id;
    let response = await fetch(url);
    return await response.json();
}

async function getRolesOption() {
    await fetch("http://localhost:8082/admin/roles")
        .then(response => response.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.roleName.substring(5);
                $('#selectNewRoles')[0].appendChild(el);
            })
        })
}

async function newUser() {
    await getRolesOption();

    const form = document.forms["formNewUser"];

    form.addEventListener('submit', addNewUser)

    async function addNewUser(e) {
        e.preventDefault();
        let newUserRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) newUserRoles.push({
                id: form.roles.options[i].value,
                roleName: form.roles.options[i].text
            })
        }

        fetch("http://localhost:8082/admin/all", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                firstName: form.firstName.value,
                lastName: form.lastName.value,
                age: form.age.value,
                email: form.email.value,
                password: form.password.value,
                role: newUserRoles
            })
        }).then(() => {
                form.reset();
                getAllUser();
                $(`.nav-tabs a[href="#nav-home"]`).tab("show");
            })
    }
}

function removeUser(){
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();
        fetch("http://localhost:8080/admin/del/" + deleteForm.id.value, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(() => {
                getAllUser();
                $('#buttonDelFormClose').click();

            })
    })
}

$('#delModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('userid');
    showDeleteModal(id);
})

async function showDeleteModal(id) {
    let user = await getUser(id);

    let form = document.forms["formDeleteUser"];
    form.id.value = user.id;
    form.firstName.value = user.firstName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = user.password;

    $('#selectDelRoles').empty();

    user.role.forEach(role => {
        let el = document.createElement("option");
        el.text = role.roleName.substring(5);
        el.value = role.id;
        $('#selectDelRoles')[0].appendChild(el);
    });
}

function updateUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", async ev => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.rolesEdit.options.length; i++) {
            if (editForm.rolesEdit.options[i].selected) editUserRoles.push({
                id: editForm.rolesEdit.options[i].value,
                roleName: editForm.rolesEdit.options[i].text
            })
        }

        fetch("http://localhost:8082/admin/update/" + editForm.editId.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                //id: editForm.editId.value,
                firstName: editForm.firstNameEdit.value,
                lastName: editForm.lastNameEdit.value,
                age: editForm.ageEdit.value,
                email: editForm.emailEdit.value,
                password: editForm.passwordEdit.value,
                role: editUserRoles
            })
        }).then(() => {
            getAllUser();
            $('#buttonEditFormClose').click();
        })
    })
}

$('#editModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('userid');
    showEditModal(id);
})

async function showEditModal(id) {
    let user = await getUser(id);

    let form = document.forms["formEditUser"];
    form.editId.value = user.id;
    form.firstNameEdit.value = user.firstName;
    form.lastNameEdit.value = user.lastName;
    form.ageEdit.value = user.age;
    form.emailEdit.value = user.email;
    form.passwordEdit.value = user.password;

    $('#selectUpdateRoles').empty();

    await fetch("http://localhost:8082/admin/roles")
        .then(response => response.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.roleName.substring(5);
                $('#selectUpdateRoles')[0].appendChild(el);
            })
        })
}