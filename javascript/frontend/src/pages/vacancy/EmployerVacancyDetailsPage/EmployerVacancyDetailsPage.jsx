import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import './EmployerVacancyDetailsPage.css';
import {experienceLevelsMap, vacancyStatusMap, workTypesMap} from "../../../constants/Common.jsx";
import {
    archiveEmployerVacancy,
    editEmployerVacancyVisibility,
    getEmployerVacancyDetails
} from "../../../services/VacancyService.js";
import {formatSalary, getDateAndTime} from "../../../utils/utils.js";
import {apiCall} from "../../../utils/api.js";

export default function EmployerVacancyDetailsPage() {
    const {id} = useParams();
    const navigate = useNavigate();
    const [vacancyDetails, setVacancyDetails] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('details'); // 'details' or 'responses'

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await getEmployerVacancyDetails(id);
                setVacancyDetails(response);
            } catch (err) {
                setError('Не удалось загрузить информацию о вакансии');
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, [id]);

    const formatDate = (timestamp) => {
        return new Date(timestamp).toLocaleDateString('ru-RU', {
            day: 'numeric',
            month: 'long',
            year: 'numeric'
        });
    };

    const handleBackClick = () => {
        navigate("/employer-vacancies");
    };

    const handleArchiveClick = () => {
        editEmployerVacancyVisibility(id, "ARCHIVED")
        navigate(0)
    };

    const handleEditClick = () => {
        navigate(`/employer-vacancies/${id}/edit`);
    };

    const handleResponseAction = (responseId, action) => {
        // TODO: Implement response actions (accept/reject)
        apiCall("")
        console.log(`Action ${action} for response ${responseId}`);
    };

    if (loading) {
        return <div className="employer-vacancy-details loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="employer-vacancy-details error">{error}</div>;
    }

    if (!vacancyDetails) {
        return <div className="employer-vacancy-details error">Вакансия не найдена</div>;
    }

    function handlePaymentClick() {
        // todo: implement payment
    }

    function handleUnarchiveClick() {
        editEmployerVacancyVisibility(id, "ACTIVE")
        navigate(0)
    }


    return (
        <div className="employer-vacancy-details">
            <div className="header-actions">
                <button className="back-button" onClick={handleBackClick}>
                    ← Назад к списку
                </button>
                {(() => {
                    switch (vacancyDetails.status) {
                        case 'ACTIVE':
                            return (
                                <button
                                    className="archive-button"
                                    onClick={handleArchiveClick}
                                >
                                    Архивировать вакансию
                                </button>
                            );
                        case 'WAITING_FOR_PAYMENT':
                            return (
                                <button
                                    className="payment-button"
                                    onClick={handlePaymentClick}
                                >
                                    Оплатить вакансию
                                </button>
                            );
                        case 'ARCHIVED':
                            return (
                                <button
                                    className="unarchive-button"
                                    onClick={handleUnarchiveClick}
                                >
                                    Разархивировать
                                </button>
                            );
                        default:
                            return null;
                    }
                })()}
                <button className="edit-button" onClick={handleEditClick}>
                    Редактировать вакансию
                </button>
            </div>

            <div className="details-header">
                <h1 className="details-title">{vacancyDetails.title}</h1>
                <div className="details-salary">{formatSalary(vacancyDetails.salaryMin, vacancyDetails.salaryMax)}</div>
            </div>

            <div className="tabs">
                <button
                    className={`tab ${activeTab === 'details' ? 'active' : ''}`}
                    onClick={() => setActiveTab('details')}
                >
                    Детали вакансии
                </button>
                <button
                    className={`tab ${activeTab === 'responses' ? 'active' : ''}`}
                    onClick={() => setActiveTab('responses')}
                >
                    Отклики ({vacancyDetails.applies.length || 0})
                </button>
            </div>

            <div className="tab-content">
                {activeTab === 'details' ? (
                    <div className="details-info">
                        <div className="details-section">
                            <h2>Основная информация</h2>
                            <div className="details-grid">
                                <div className="details-item">
                                    <span className="details-label">Тип работы:</span>
                                    <span className="details-value">{workTypesMap[vacancyDetails.workType]}</span>
                                </div>
                                <div className="details-item">
                                    <span className="details-label">Опыт:</span>
                                    <span
                                        className="details-value">{experienceLevelsMap[vacancyDetails.experienceLevel]}</span>
                                </div>
                                <div className="details-item">
                                    <span className="details-label">Локация:</span>
                                    <span className="details-value">{vacancyDetails.location || 'Не указана'}</span>
                                </div>
                                <div className="details-item">
                                    <span className="details-label">Статус:</span>
                                    <span
                                        className="details-value status">{vacancyStatusMap[vacancyDetails.status]}</span>
                                </div>
                            </div>
                        </div>

                        <div className="details-section">
                            <h2>Описание</h2>
                            <p className="details-description">{vacancyDetails.description}</p>
                        </div>

                        <div className="details-section">
                            <h2>Статистика</h2>
                            <div className="stats-grid">
                                <div className="stat-item">
                                    <span className="stat-value">{vacancyDetails.applies.length || 0}</span>
                                    <span className="stat-label">Откликов</span>
                                </div>
                            </div>
                        </div>

                        <div className="details-section">
                            <h2>Дополнительная информация</h2>
                            <div className="details-grid">
                                <div className="details-item">
                                    <span className="details-label">Дата публикации:</span>
                                    <span className="details-value">{formatDate(vacancyDetails.createdAtMillis)}</span>
                                </div>
                                <div className="details-item">
                                    <span className="details-label">Дата обновления:</span>
                                    <span className="details-value">{formatDate(vacancyDetails.editedAtMillis)}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="responses-section">
                        {vacancyDetails.applies && vacancyDetails.applies.length > 0 ? (
                            <div className="responses-list">
                                {vacancyDetails.applies.map((applyDto) => (
                                    <div key={applyDto.resumeId} className="response-card">
                                        <div className="response-header">
                                            <div className="applicant-info">
                                                <h3>{applyDto.firstName + " " + applyDto.lastName}</h3>
                                                <h4>{applyDto.resumeTitle}</h4>
                                                <span className="response-date">
                                                    Дата отклика: {getDateAndTime(applyDto.appliedAt)}
                                                </span>
                                            </div>
                                            {/*<div className="response-status">*/}
                                            {/*    <span className={`status-badge ${applyDto.status.toLowerCase()}`}>*/}
                                            {/*        {applyDto.status}*/}
                                            {/*    </span>*/}
                                            {/*</div>*/}
                                        </div>
                                        <p className="response-message">{applyDto.message}</p>
                                        <div className="response-actions">
                                            <button
                                                className="view-profile-btn"
                                                onClick={() => navigate(`/resumes/${applyDto.resumeId}`)}
                                            >
                                                Просмотр резюме
                                            </button>
                                            {applyDto.status === 'PENDING' && (
                                                <>
                                                    <button
                                                        className="accept-btn"
                                                        onClick={() => handleResponseAction(applyDto.id, 'ACCEPT')}
                                                    >
                                                        Принять
                                                    </button>
                                                    <button
                                                        className="reject-btn"
                                                        onClick={() => handleResponseAction(applyDto.id, 'REJECT')}
                                                    >
                                                        Отказать
                                                    </button>
                                                </>
                                            )}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="no-responses">
                                Пока нет откликов на вакансию
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}
