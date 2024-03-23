const filterFrom = document.querySelector('.filterFromPage');
if (filterFrom) {
    document.getElementById('filterForm').addEventListener('submit', function (e) {
        e.preventDefault();
        const sizes = [];
        const colors = [];
        document.querySelectorAll('input[name="size"]:checked').forEach((checkbox) => {
            sizes.push(checkbox.value);
        });
        document.querySelectorAll('input[name="color"]:checked').forEach((checkbox) => {
            colors.push(checkbox.value);
        });

        const searchParams = new URLSearchParams();
        if (sizes.length > 0) {
            sizes.forEach(size => {
                searchParams.append('sizes', size);
            });
        }
        if (colors.length > 0) {
            colors.forEach(color => {
                searchParams.append('colors', color);
            });
        }
        window.location.href = '/mangostore/product?' + searchParams.toString();
    });
}