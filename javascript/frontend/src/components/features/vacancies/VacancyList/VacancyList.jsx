import './VacancyList.css';

const experienceLevelMap = {
    NO_EXPERIENCE: 'Без опыта',
    ONE_YEAR: '1 год',
    THREE_YEARS: '3 года',
    SIX_PLUS_YEARS: '6+ лет'
};

const workTypeMap = {
    REMOTE: 'Удаленно',
    OFFICE: 'В офисе',
    HYBRID: 'Гибридный'
};

const formatSalary = (min, max) => {
    if (!min?.amount && !max?.amount) return 'Зарплата не указана';
    if (!min?.amount) return `до ${max.amount.toLocaleString()} ${max.currency}`;
    if (!max?.amount) return `от ${min.amount.toLocaleString()} ${min.currency}`;
    return `${min.amount.toLocaleString()} - ${max.amount.toLocaleString()} ${min.currency}`;
};

export function VacancyList({ vacancies, currentPage, totalPages, onPageChange }) {
    const handleResponse = (vacancyId) => {
        // TODO: Implement response logic (apply)
        console.log('Responding to vacancy:', vacancyId);
    };

    const renderPagination = () => {
        const pages = [];
        const maxVisiblePages = 5;
        let startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
        let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

        if (endPage - startPage + 1 < maxVisiblePages) {
            startPage = Math.max(1, endPage - maxVisiblePages + 1);
        }

        // Добавляем кнопку "Предыдущая"
        pages.push(
            <button
                key="prev"
                className={`pagination-button ${currentPage === 1 ? 'disabled' : ''}`}
                onClick={() => currentPage > 1 && onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
            >
                ←
            </button>
        );

        // Добавляем первую страницу
        if (startPage > 1) {
            pages.push(
                <button
                    key={1}
                    className="pagination-button"
                    onClick={() => onPageChange(1)}
                >
                    1
                </button>
            );
            if (startPage > 2) {
                pages.push(<span key="start-ellipsis" className="pagination-ellipsis">...</span>);
            }
        }

        // Добавляем страницы
        for (let i = startPage; i <= endPage; i++) {
            pages.push(
                <button
                    key={i}
                    className={`pagination-button ${currentPage === i ? 'active' : ''}`}
                    onClick={() => onPageChange(i)}
                >
                    {i}
                </button>
            );
        }

        // Добавляем последнюю страницу
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                pages.push(<span key="end-ellipsis" className="pagination-ellipsis">...</span>);
            }
            pages.push(
                <button
                    key={totalPages}
                    className="pagination-button"
                    onClick={() => onPageChange(totalPages)}
                >
                    {totalPages}
                </button>
            );
        }

        // Добавляем кнопку "Следующая"
        pages.push(
            <button
                key="next"
                className={`pagination-button ${currentPage === totalPages ? 'disabled' : ''}`}
                onClick={() => currentPage < totalPages && onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
            >
                →
            </button>
        );

        return pages;
    };

    return (
        <div className="vacancy-container">
            <div className="vacancy-list">
                {vacancies.map((vacancy) => (
                    <div key={vacancy.id} className="vacancy-card">
                        <div className="vacancy-header">
                            <h3 className="vacancy-title">{vacancy.title}</h3>
                            <div className="vacancy-salary">
                                {formatSalary(vacancy.salaryMin, vacancy.salaryMax)}
                            </div>
                        </div>
                        <div className="vacancy-info">
                            <span className="vacancy-type">{workTypeMap[vacancy.workType]}</span>
                            {vacancy.location && (
                                <span className="vacancy-location">{vacancy.location}</span>
                            )}
                        </div>
                        <p className="vacancy-description">{vacancy.description}</p>
                        <div className="vacancy-footer">
                            <span className="vacancy-experience">{experienceLevelMap[vacancy.experienceLevel]}</span>
                            <span className="vacancy-views">Просмотров: {vacancy.viewsCount}</span>
                        </div>
                        <button 
                            className="vacancy-response-button"
                            onClick={() => handleResponse(vacancy.id)}
                        >
                            Откликнуться
                        </button>
                    </div>
                ))}
            </div>
            <div className="pagination">
                {renderPagination()}
            </div>
        </div>
    );
} 