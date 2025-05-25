import React from 'react';
import { Card } from '@hh.ru/magritte-ui-card';
import { Text } from '@hh.ru/magritte-ui-typography';
import styles from './SelectedState.module.css';
import type { Question } from '../../pages/ServicePickerPage/types/question';

interface SelectedStateProps {
    questions: Question[];
    answers: Record<number, any>;
    getDisplayValue: (questionId: number, value: any) => string[];
}

export const SelectedState: React.FC<SelectedStateProps> = ({
    questions,
    answers,
    getDisplayValue
}) => {
    const renderAnswer = (question: Question) => {
        const value = answers[question.id];
        if (value === undefined || value === null) return null;

        const displayValues = getDisplayValue(question.id, value);
        if (!displayValues?.length) return null;

        return (
            <div className={styles.parameter} key={question.id}>
                <Text>{question.questionText}:</Text>
                <Text weight="medium">
                    {displayValues.join(', ')}
                </Text>
            </div>
        );
    };

    const hasAnswers = questions.some(question => {
        const value = answers[question.id];
        return value !== undefined && value !== null && 
               (Array.isArray(value) ? value.length > 0 : true);
    });

    if (!hasAnswers) {
        return null;
    }

    return (
        <div className={styles.container}>
            <Card className={styles.card}>
                <h3 className={styles.title}>
                    Выбранные параметры
                </h3>
                <div className={styles.content}>
                    {questions.map(question => renderAnswer(question))}
                </div>
            </Card>
        </div>
    );
}; 