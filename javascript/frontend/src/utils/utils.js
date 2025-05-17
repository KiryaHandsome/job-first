export const getDateAndTime = (timestamp) => {
    if (!timestamp) return 'Не указано';

    return new Date(timestamp).toLocaleDateString('ru-RU', {
        year: 'numeric',
        month: 'long',
        day: "numeric",
        hour: "numeric",
        minute: "numeric"
    });
};

export const getDate = (timestamp) => {
    if (!timestamp) return 'Не указано';

    return new Date(timestamp).toLocaleDateString('ru-RU', {
        year: 'numeric',
        month: 'long',
    });
};

export const formatSalary = (min, max) => {
    if (!min?.amount && !max?.amount) return 'По договоренности';
    if (!min?.amount) return `до ${max.amount.toLocaleString()} ${max.currency}`;
    if (!max?.amount) return `от ${min.amount.toLocaleString()} ${min.currency}`;
    return `${min.amount.toLocaleString()} - ${max.amount.toLocaleString()} ${min.currency}`;
};