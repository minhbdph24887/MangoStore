function add() {
    if (!confirm('Do you want add ?')) {
        return false
    } else {
        alert('Add Successfully');
    }
}

function update() {
    if (!confirm('Do you want update ?')) {
        return false
    } else {
        alert('Update Successfully');
    }
}

function restore() {
    if (!confirm('Do you want restore ?')) {
        return false
    } else {
        alert('Restore Successfully');
    }
}

function remove() {
    if (!confirm('Do you want delete ?')) {
        return false
    } else {
        alert('Delete Successfully');
    }
}

function logOut() {
    return confirm('You want to log out of MangoStore ?');
}