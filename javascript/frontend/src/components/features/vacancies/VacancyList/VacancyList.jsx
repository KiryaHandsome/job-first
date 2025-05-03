import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import './VacancyList.css';
import {experienceLevelsMap, workTypesMap} from "../../../../constants/Common.jsx";
import {apiCall} from "../../../../utils/api.js";
import Notification from '../../../common/Notification/Notification';
import {applyToVacancy} from "../../../../services/VacancyService.js";

const formatSalary = (min, max) => {
    if (!min?.amount && !max?.amount) return 'Зарплата не указана';
    if (!min?.amount) return `до ${max.amount.toLocaleString()} ${max.currency}`;
    if (!max?.amount) return `от ${min.amount.toLocaleString()} ${min.currency}`;
    return `${min.amount.toLocaleString()} - ${max.amount.toLocaleString()} ${min.currency}`;
};

export function VacancyList({vacancies, currentPage, totalPages, onPageChange}) {
    const [notification, setNotification] = useState(null);
    const navigate = useNavigate();

    const handleVacancyClick = (vacancyId) => {
        navigate(`/vacancies/${vacancyId}`);
    };

    const handleApply = async (vacancyId) => {
        try {
            const response = await applyToVacancy(vacancyId);

            if (response.code === "vacancy.already_applied") {
                setNotification({
                    type: 'error',
                    message: 'Вы уже откликнулись на эту вакансию'
                });
            } else {
                setNotification({
                    type: 'success',
                    message: 'Вы успешно откликнулись на вакансию'
                });
            }
        } catch (error) {
            console.error('Error applying to vacancy:', error);
            setNotification({
                type: 'error',
                message: 'Произошла ошибка при отклике на вакансию'
            });
        }
    };

    const closeNotification = () => {
        setNotification(null);
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
            {notification && (
                <Notification
                    type={notification.type}
                    message={notification.message}
                    onClose={closeNotification}
                />
            )}
            <div className="vacancy-list">
                {vacancies.map((vacancy) => (
                    <div
                        key={vacancy.id}
                        className="vacancy-card"
                        onClick={() => handleVacancyClick(vacancy.id)}
                    >
                        <div className="vacancy-header">
                            <div className="vacancy-title">{vacancy.title}</div>
                            <div className="vacancy-salary">
                                {formatSalary(vacancy.salaryMin, vacancy.salaryMax)}
                            </div>
                        </div>
                        <div className="vacancy-info">
                            <span className="vacancy-type">{workTypesMap[vacancy.workType]}</span>
                            {vacancy.location && (
                                <span className="vacancy-location">{vacancy.location}</span>
                            )}
                        </div>
                        <p className="vacancy-description">{vacancy.description}</p>
                        <div className="vacancy-footer">
                            <span className="vacancy-experience">{experienceLevelsMap[vacancy.experienceLevel]}</span>
                            <span className="vacancy-views">Просмотров: {vacancy.viewsCount}</span>
                        </div>
                        <button
                            className={`vacancy-response-button ${vacancy.userApplied ? 'applied' : ''}`}
                            onClick={!vacancy.userApplied ? () => handleApply(vacancy.id) : undefined}
                            disabled={vacancy.userApplied}
                        >
                            {vacancy.userApplied ? 'Вы уже откликнулись' : 'Откликнуться'}
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